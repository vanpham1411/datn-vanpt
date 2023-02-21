package com.example.datn.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "order_item")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItem {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "order_id")
    private Long orderID;
    @Column(name = "item_id")
    private Long itemID;
    @Column(name = "quantity")
    private int quantity;
}
