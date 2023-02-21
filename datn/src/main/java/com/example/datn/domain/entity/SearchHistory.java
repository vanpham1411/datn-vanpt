package com.example.datn.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.print.attribute.standard.MediaSize;
import java.sql.Timestamp;

@Table(name = "search_history")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchHistory {
    @Id
    @Column
    private Long id;
    @Column(name = "user_id")
    private Long userID;
    @Column(name = "content")
    private String content;
    @Column(name = "time")
    private Timestamp timestamp;


}
