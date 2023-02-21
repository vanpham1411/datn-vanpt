package com.example.datn.controller;

import com.example.datn.domain.utility.FilterParam;
import com.example.datn.service.ProductSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ProductSuggestionController {
    @Autowired
    ProductSuggestionService productSuggestionService;

    @PostMapping("/Product/Related")
    public ResponseEntity getRelated(@RequestBody FilterParam param) {
        return ResponseEntity.ok(new ResponseBody(
                productSuggestionService.getRelated(param.getParentID()),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

    @PostMapping("/Product/Suggestion")
    public ResponseEntity getSuggestion(@RequestBody FilterParam param) {
        return ResponseEntity.ok(new ResponseBody(
                productSuggestionService.getSuggestion(param.getUserID()),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }
}
