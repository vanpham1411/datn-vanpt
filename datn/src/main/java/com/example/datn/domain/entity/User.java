package com.example.datn.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_id")
    private Long userID;

    @Column
    private String name;
    @Column(name = "mobile")
    private String phone;
    @Column
    private String address;
    @Column
    private String password;
    @Column
    private int role;

}
