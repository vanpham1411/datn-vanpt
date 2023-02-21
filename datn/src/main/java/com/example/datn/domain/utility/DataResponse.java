package com.example.datn.domain.utility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataResponse {
    Object data;
    int totalPage;
    int totalRecord;
}
