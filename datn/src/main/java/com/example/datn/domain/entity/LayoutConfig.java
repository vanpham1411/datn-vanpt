package com.example.datn.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "layout_config")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LayoutConfig {
    @Id
    @Column(name = "layout_id")
    @JsonProperty("LayoutId")
    private int id;
    @Column(name = "layout_name")
    @JsonProperty("LayoutName")
    private String layoutName;
    @Column(name = "layout_value")
    @JsonProperty("LayoutValue")
    private String layoutValue;

}