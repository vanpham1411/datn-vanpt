package com.example.datn.service;

import com.example.datn.domain.entity.Item;
import com.example.datn.domain.entity.Product;
import com.example.datn.domain.entity.Type;
import com.example.datn.domain.entity.ViewProduct;
import com.example.datn.domain.model.ItemModel;
import com.example.datn.domain.model.ProductItem;
import com.example.datn.domain.model.ProductRevenue;
import com.example.datn.domain.repository.*;
import com.example.datn.domain.utility.DataResponse;
import com.example.datn.domain.utility.FilterParam;
import com.example.datn.domain.utility.SequenceGenerator;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

@Service
@Slf4j
public class ProductService {
    @Autowired
    public ProductRepository productRepository;
    @Autowired
    public ItemRepository itemRepository;
    @Autowired
    public TypeRepository typeRepository;
    @Autowired
    public ViewProductRepository viewProductRepository;

    @Autowired
    public SuggestUserRepository suggestUserRepository;

    @Autowired
    private ProductESRepository productESRepository;
    public List<Product> getListProductInfo(int limit, int offset) {
        DataResponse res  = productESRepository.search(null, 0, 0, 20);
        return (List<Product>) res.getData();
    }

    public void syncDataToES() {
        List<Product> productList = getListProductInfo(50, 0);
        log.info("get size of list: {}", productList.size());
        for (Product product : productList) {
            productESRepository.addProduct(product);
        }
    }



    public Product getProductInfo(long productID) {
        Optional<Product> product = productRepository.findById(productID);
        if(!product.isPresent() || product.get().getStatus() == -1) return null;
        return product.get();
    }

    public List<Product> getRelated(long productID) {
        Product product = getProductInfo(productID);

        return productESRepository.getRelated(product);

    }

    public DataResponse productFilter(FilterParam param) {
        log.info("param: {}",param.getParentID());
        return productESRepository.search(param.getKeyword(), param.getParentID(), param.offset(), param.limit());

//        List<Product> productList;
//        if(param.getKeyword() == null && param.getParentID() == 0) {
//            productList = productRepository.findAll();
//        }
//        else if( param.getParentID() == 0) {
//            productList = productRepository.filterProduct(param.getKeyword());
//        }
//        else productList = productRepository.filterProductByCategory(param.getKeyword(), param.getParentID());
//        return DataResponse.builder().data(productList)
//                .totalRecord(productList.size())
//                .totalPage(1).build();
//

    }

    public List<ProductRevenue> productSold(FilterParam param) {
       List<List<Object>> list = productRepository.getPopularProduct(param.getCreateDateMin(), param.getCreateDateMax());
       if(list != null && list.size()>0) log.info("get list: {}", list.get(0).get(0));
       List<ProductRevenue> productRevenueList = new ArrayList<>();
       for(List<Object> obj : list) {
           BigInteger integer = (BigInteger) obj.get(0);
           BigDecimal quantity = (BigDecimal) obj.get(2);
           Integer cost = (Integer) obj.get(3);
           long amount = (long) quantity.intValue() * cost;
           ProductRevenue revenue = new ProductRevenue(integer.longValue(), (String)obj.get(1), quantity.intValue(), amount);
//           ProductRevenue revenue = new ProductRevenue(integer.longValue(), (String)obj.get(1), quantity.intValue(), (long)Long.valueOf(cost));

           productRevenueList.add(revenue);
       }
        return productRevenueList;
    }

    public ProductItem getProductDetail(long productId) {
        Product product = getProductInfo(productId);
        if(product == null ) return null;
        List<Item> itemList = itemRepository.getAllItemOfProductID(productId);
        List<ItemModel> itemModelList = new ArrayList<>();
        for(Item item: itemList) {
            Type type = typeRepository.findById(item.getTypeID()).get();
            ItemModel itemModel = ItemModel.convertFromItem(item,type);
            itemModelList.add(itemModel);
        }
        ProductItem productItem = new ProductItem(product, itemModelList);
        return productItem;
    }

    public int updateView(long userID, long productID) {

        ViewProduct viewProduct = ViewProduct.builder().productID(productID)
                .userID(userID)
                .timestamp(new Timestamp(System.currentTimeMillis())).build();
        viewProductRepository.save(viewProduct);
        return 1;

    }

    public int updateProduct(ProductItem productItem) {
        Map<Long, Type> typeMap = new HashMap<>();
        log.info("get body request: {}", productItem);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        ProductItem productItemOld = getProductDetail(productItem.getProduct().getProductID());
        Product product = productItemOld.getProduct();
        Product newProduct = productItem.getProduct();
        newProduct.setUpdateTime(now);
        newProduct.setSoldQuantity(product.getSoldQuantity());

        productRepository.save(newProduct);
        productESRepository.updateProduct(newProduct);
        List<Item> itemList = new ArrayList<>();
        List<ItemModel> itemModelListOld = productItemOld.getItems();
        List<Long> listItemID = new ArrayList<>();
        for (ItemModel itemModel : productItem.getItems()) {

            long itemID = itemModel.getItemID() != 0 ? itemModel.getItemID() : SequenceGenerator.getInstance().nextId();
            listItemID.add(itemID);
            Type type = itemModel.getType();
            if(type.getTypeID() == null||type.getTypeID() == 0) {
                long typeID = SequenceGenerator.getInstance().nextId();
                type.setTypeID(typeID);
            }
            Item item = Item.builder()
                    .itemID(itemID)
                    .productID(product.getProductID())
                    .quantity(itemModel.getQuantity())
                    .size(itemModel.getSize())
                    .typeID(type.getTypeID())
                    .build();
            typeMap.put(type.getTypeID(),type);
            itemList.add(item);
        }
        typeRepository.saveAll(typeMap.values());
        itemRepository.saveAll(itemList);
        for(ItemModel itemModel : itemModelListOld) {
            if(!listItemID.contains(itemModel.getItemID())) {
                itemRepository.deleteById(itemModel.getItemID());
                typeRepository.deleteById(itemModel.getType().getTypeID());
            }
        }

        return 1;
    }

    public int createProduct(ProductItem productItem) {
        if(productRepository.existsByProductCode(productItem.getProduct().getProductCode())) {
            return 0;
        }
        if(productItem.getProduct().getProductID() == null) {
            productItem.getProduct().setProductID(SequenceGenerator.getInstance().nextId());
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        int res = saveProduct(productItem,now, now, 0);
        productESRepository.addProduct(productItem.getProduct());
        return 1;

    }
    @Transactional
    int saveProduct(ProductItem productItem, Timestamp createTime, Timestamp updateTime, int soldQuantity) {
        long productID = productItem.getProduct().getProductID() == null ? SequenceGenerator.getInstance().nextId() : productItem.getProduct().getProductID();
        Map<String, Type> typeMap = new HashMap<>();
        Product product = productItem.getProduct();
        if(product.getProductID() == null) {
            product.setProductID(productID);
        }
        int quantity =0;
        for (ItemModel itemModel : productItem.getItems()){
            quantity += itemModel.getQuantity();
        }
        product.setCreateTime(createTime);
        product.setUpdateTime(updateTime);
        product.setSoldQuantity(soldQuantity);
        product.setQuantity(quantity);
        productRepository.save(product);
        List<Type> typeList = new ArrayList<>();
        List<Item> itemList = new ArrayList<>();

        for (ItemModel itemModel : productItem.getItems()) {
            long itemID = SequenceGenerator.getInstance().nextId();
            Type type = itemModel.getType();
            long typeID = SequenceGenerator.getInstance().nextId();
            type.setTypeID(typeID);
            Item item = Item.builder()
                    .itemID(itemID)
                    .productID(productID)
                    .quantity(itemModel.getQuantity())
                    .size(itemModel.getSize())
                    .typeID(typeID)
                    .build();
            typeList.add(type);
            typeMap.put(type.getName(),type);
            itemList.add(item);
            typeRepository.save(type);
            itemRepository.save(item);
        }
        typeRepository.saveAll(typeMap.values());
        itemRepository.saveAll(itemList);
        return 0;
    }


    public int deleteProduct(long productId) {
        productRepository.removeProduct(productId);
        Optional<Product> product = productRepository.findById(productId);
        if(product.isPresent()) {
            productESRepository.updateProduct(product.get());
        }
//        List<Long> typeIDList = itemRepository.getTypeOfProduct(productId);
//        itemRepository.deleteAllByProduct(productId);
//        typeIDList.forEach(typeID -> {
//            typeRepository.deleteById(typeID);
//        });
        return 0;
     }
     public int deleteListProduct(List<Long> productIDList) {
        productIDList.forEach(productID -> {
            deleteProduct(productID);
        });
        return productIDList.size();
     }

     public int deleteByCategory(long categoryID) {
        int res = productRepository.deleteInCategory(categoryID);
         List<Optional<Product> > optionalList = productRepository.findAllByCategoryID(categoryID);
        optionalList.forEach(optional -> {
            if(optional.isPresent()) productESRepository.updateProduct(optional.get());
        });
         return res;
     }


     public Type getType(long typeID) {

        return typeRepository.findById(typeID).get();
     }
}
