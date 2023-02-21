package com.example.datn.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRevenue {
    private long productID;
    private String name;
    private int quantity;
    private long amount;
}
