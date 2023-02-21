package com.example.datn.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Table(name = "view_product")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
public class ViewProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(name = "product_id")
    private Long productID;
    @Column(name = "user_id")
    private Long userID;
    @Column(name = "timestamp")
    private Timestamp timestamp;

}
