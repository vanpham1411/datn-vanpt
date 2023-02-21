package com.example.datn.controller;

import com.example.datn.domain.entity.CartItem;
import com.example.datn.domain.entity.Item;
import com.example.datn.domain.utility.FilterParam;
import com.example.datn.service.CartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin
public class CartController {
    @Autowired
    CartService cartService;
    @PostMapping("/CartDetail/GetFilterPaging")
    public ResponseEntity getFilter(@RequestBody FilterParam param) {
        return ResponseEntity.ok(new ResponseBody(
                cartService.getFilterPaging(param),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

    @PostMapping("/CartDetail")
    public ResponseEntity post(@RequestBody CartItem item) {
        return ResponseEntity.ok(new ResponseBody(
                cartService.addToCart(item),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

    @PutMapping("/CartDetail")
    public ResponseEntity put(@RequestBody CartItem item) {
        return ResponseEntity.ok(new ResponseBody(
                cartService.updateFromCart(item),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }


    @DeleteMapping("/CartDetail/DeleteMulti")
    public ResponseEntity deleteMulti(@RequestBody List<CartItem> itemList) {
        return ResponseEntity.ok(new ResponseBody(
                cartService.removeFromCart(itemList),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

}
