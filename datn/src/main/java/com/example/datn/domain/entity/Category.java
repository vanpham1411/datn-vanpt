package com.example.datn.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Id
    @Column
    private Long categoryId;

    @Column(name = "code")
    private String code;
    @Column
    private String name;
    @Column(name = "info")
    private String description;
    @Column(name = "deleted")
    private int isDeleted;
}
