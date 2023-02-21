package com.example.datn.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Type {
    @Id
    @Column(name = "type_id")
    private Long typeID;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String describe;
}
