package com.example.datn.domain.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilterParam {
    private String keyword;
    private Integer status;
    private long parentID;
    private int pageSize;
    private int pageNumber;
    private Timestamp createDateMin;
    private Timestamp createDateMax;
    private Long userID;

    public int limit() {
        return this.pageSize==0 ? 30 : this.pageSize;
    }
    public int offset() {
        return (this.pageNumber - 1)* pageSize;
    }
}
