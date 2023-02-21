package com.example.datn.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    private long productID;
    private String productCode;

    private String productName;

    private long itemID;
    private long typeID;
    private String typeName;
    private String size;
    private int price;
//    order Item Info
    private long orderItemID;
    private long orderID;
    private int quantity;
    private String imageURL;

}
