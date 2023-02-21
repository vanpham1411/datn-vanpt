package com.example.datn.service;

import com.example.datn.domain.entity.CartItem;
import com.example.datn.domain.entity.Item;
import com.example.datn.domain.model.CartItemModel;
import com.example.datn.domain.model.ProductItem;
import com.example.datn.domain.repository.CartItemRepository;
import com.example.datn.domain.repository.ItemRepository;
import com.example.datn.domain.utility.DataResponse;
import com.example.datn.domain.utility.FilterParam;
import com.example.datn.domain.utility.SequenceGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CartService {
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ProductService productService;
    @Autowired
    ItemRepository itemRepository;

    public DataResponse getFilterPaging(FilterParam param) {
        List<CartItem> cartItemList = cartItemRepository.findAllByUserID(param.getUserID());
        List<CartItemModel> cartItemModelList = new ArrayList<>();
        cartItemList.forEach(cartItem -> {
            long productID = itemRepository.getProductID(cartItem.getItemID());
            ProductItem productItem = productService.getProductDetail(productID);
            CartItemModel cartItemModel = CartItemModel.builder()
                    .cartItem(cartItem)
                    .productItem(productItem).build();
            cartItemModelList.add(cartItemModel);
        });
        return DataResponse.builder().data(cartItemModelList)
                .totalRecord(cartItemModelList.size())
                .totalPage(1).build();
    }

    public CartItem addToCart(CartItem cartItem) {

        cartItem.setId(SequenceGenerator.getInstance().nextId());
        log.info("get item: {}", cartItem);
        Item item = itemRepository.findById(cartItem.getItemID()).get();
        if(item.getQuantity() <cartItem.getCartQuantity()) throw new RuntimeException("quantity is not enough");
        return cartItemRepository.save(cartItem);
    }

    public int removeFromCart(List<CartItem> cartItemList) {
        cartItemRepository.deleteAll(cartItemList);
        return cartItemList.size();
    }

    public CartItem updateFromCart(CartItem item){
        return cartItemRepository.save(item);
    }

}
