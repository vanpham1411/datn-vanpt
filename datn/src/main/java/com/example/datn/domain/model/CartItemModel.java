package com.example.datn.domain.model;

import com.example.datn.domain.entity.CartItem;
import com.example.datn.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemModel{
    ProductItem productItem;
    CartItem cartItem;


    private int quantity;


}
