package com.example.datn.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "item")
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class Item {
    @Id
    @Column(name = "itemId")
    private Long itemID;
    @Column(name = "product_id")
    private Long productID;

    @Column
    private String size;

    @Column(name = "type_id")
    private Long typeID;
    @Column(name = "quantity")
    private int quantity;


}
