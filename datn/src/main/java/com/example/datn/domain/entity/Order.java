package com.example.datn.domain.entity;

import com.example.datn.domain.utility.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "product_order")
@Builder
public class Order {
    @Id
    @Column(name = "order_id")
    private Long orderID;

    @Column(name = "user_id")
    private Long userID;
    @Column(name = "create_time")
    @JsonSerialize(using = JsonDateSerializer.class)
    private Timestamp createTime;
    @Column
    private int status;
    @Column
    private long cost;
    @Column
    private String address;
    @Column
    private String phone;

    @Column(name = "user_name")
    private String userName;



}
