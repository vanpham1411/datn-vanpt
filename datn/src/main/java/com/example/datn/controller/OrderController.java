package com.example.datn.controller;

import com.example.datn.domain.entity.Order;
import com.example.datn.domain.model.OrderModel;
import com.example.datn.domain.utility.FilterParam;
import com.example.datn.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/Order/GetFilterPaging")
    public ResponseEntity getFilter(@RequestBody FilterParam param) {
        return ResponseEntity.ok(new ResponseBody(
                orderService.getFilterPaging(param),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

    @PostMapping("/Order/AddOrder")
    public ResponseEntity post(@RequestBody OrderModel orderModel) {
        List<Long> res = orderService.createOrder(orderModel);
        if(res.size() == 0)
        return ResponseEntity.ok(new ResponseBody(
                res,
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
        else return ResponseEntity.ok(new ResponseBody(
                res,
                ResponseBody.Status.FAILED,
                ResponseBody.Code.INTERNAL_ERROR
        ));
    }

    @PutMapping("/Order/UpdateEmployeeOrder")
    public ResponseEntity putStatus(@RequestBody Order order) {
        return ResponseEntity.ok(new ResponseBody(
                orderService.updateStatus(order),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

    @GetMapping("/Order/GetByCode/{id}")
    public ResponseEntity getByCode(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(new ResponseBody(
                orderService.getOrderByID(Long.parseLong(id)),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

}
