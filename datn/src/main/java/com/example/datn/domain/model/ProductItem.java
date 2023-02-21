package com.example.datn.domain.model;

import com.example.datn.domain.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
@Data
public class ProductItem {
    Product product;
    List<ItemModel> items = new ArrayList<>();


    public ProductItem(Product info, List<ItemModel> items) {
        this.product = info;
        this.items = items;
    }

}
