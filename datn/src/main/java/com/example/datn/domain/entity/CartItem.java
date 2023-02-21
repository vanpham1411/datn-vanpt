package com.example.datn.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "cart_item")
public class CartItem {
    @Id
    @Column
    private Long id;
    @Column(name = "user_id")
    private Long userID;
    @Column(name = "item_id")
    private Long itemID;
    @Column(name = "quantity")
    @JsonProperty("quantity")
    private int cartQuantity;
}
