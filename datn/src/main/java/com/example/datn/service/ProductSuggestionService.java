package com.example.datn.service;

import com.example.datn.domain.entity.Product;
import com.example.datn.domain.repository.ProductESRepository;
import com.example.datn.domain.repository.SuggestUserRepository;
import com.example.datn.domain.repository.ViewProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductSuggestionService {
    @Autowired
    ProductService productService;
    @Autowired
    ProductESRepository productESRepository;
    @Autowired
    ViewProductRepository viewProductRepository;
    @Autowired
    SuggestUserRepository suggestUserRepository;
    public List<Product> getRelated(long productID) {
        Product product = productService.getProductInfo(productID);

        List<Product> list = productESRepository.getRelated(product);
        list.forEach(product1 -> {
            if(product.getProductID() == product1.getProductID()) list.remove(product1);
        });
        return list;
    }

    public List<Product> getSuggestion(long userID) {
        Long viewRecent = viewProductRepository.getRecentView(userID);
        List<Product> listProduct = getRelated(viewRecent);
        Map<Long, Product> map = new HashMap<>();
        for(Product product: listProduct) {
            map.put(product.getProductID(), product);
        }
        List<Long> listID = suggestionByUser(userID);
        for(Long id : listID) {
            if(!map.containsKey(id)) {
                Product product = productService.getProductInfo(id);
                if(product != null)
                map.put(id, product);
            }
        }
        return listProduct;

    }

    public List<Long> suggestionByUser(long userID) {
        List<Long> inputValue = suggestUserRepository.getByID(userID);
        List<Long> outPut = suggestUserRepository.suggestProduct(inputValue);
        for(Long id: outPut) {
            if(inputValue.contains(id)) outPut.remove(id);
        }
        return outPut;
    }
}
