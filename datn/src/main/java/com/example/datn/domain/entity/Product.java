package com.example.datn.domain.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

    @Id
    @Column(name = "product_id")
    private Long productID;
    @Column(name = "category_id")
    private Long categoryID;
    @Column(name = "product_code")
    @JsonProperty("code")
    private String productCode;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    @JsonProperty("description")
    private String describe;
    @Column(name = "image_url")
    private String imageURL;
    @Column(name = "create_time")
    private Timestamp createTime;
    @Column(name = "update_time")
    private Timestamp updateTime;
    @Column
    private int status;
    @Column(name = "sold_nb")
    private int soldQuantity;
    @Column
    private int quantity;
    @Column(name = "cost")
    private int cost;

    @Column(name = "has_discount")
    private boolean hasDiscount;
//    @OneToMany(mappedBy = "product")
//    private List<Item> items;

}
