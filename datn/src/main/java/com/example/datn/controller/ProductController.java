package com.example.datn.controller;

import com.example.datn.domain.model.ProductItem;
import com.example.datn.domain.utility.FilterParam;
import com.example.datn.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin
public class ProductController {
    @Autowired
    ProductService productService;
    @PostMapping("/Product/AddProduct")
    public ResponseEntity createProduct(@RequestBody ProductItem productItem) {
        log.info("productItem: {}", productItem);
        return ResponseEntity.ok(new ResponseBody(
                productService.createProduct(productItem),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }
    @PutMapping("/Product/UpdateProduct")
    public ResponseEntity updateProduct(@RequestBody ProductItem productItem) {
        try {
            return ResponseEntity.ok(new ResponseBody(
                    productService.updateProduct(productItem),
                    ResponseBody.Status.SUCCESS,
                    ResponseBody.Code.SUCCESS
            ));
        } catch (Exception e) {
            log.error("error: {}", e);
            return ResponseEntity.ok(new ResponseBody(
                    0,
                    ResponseBody.Status.FAILED,
                    ResponseBody.Code.CLIENT_ERROR
            ));
        }
    }
    @GetMapping("/Product")
    public ResponseEntity getList(@RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
                                  @RequestParam(name = "limit", required = false, defaultValue = "20") int limit) {
        return ResponseEntity.ok(new ResponseBody(
                productService.getListProductInfo(limit,offset),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }
    @DeleteMapping("/Product/DeleteMulti")
    public ResponseEntity delete(@RequestBody List<Long> productID) {
        return ResponseEntity.ok(new ResponseBody(
                productService.deleteListProduct(productID),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

//    @GetMapping("/Product/GetByCode/{id}")
//    public ResponseEntity getDetail(@PathVariable(name = "id") String productID, @RequestBody FilterParam param) {
//        if(param.getUserID()!=0) {
//            productService.updateView(param.getUserID(), Long.parseLong(productID));
//        }
//        return ResponseEntity.ok(new ResponseBody(
//                productService.getProductDetail(Long.parseLong(productID)),
//                ResponseBody.Status.SUCCESS,
//                ResponseBody.Code.SUCCESS
//        ));
//    }

    @GetMapping("/Product/GetByCode/{id}")
    public ResponseEntity getDetail(@PathVariable(name = "id") String productID) {
        return ResponseEntity.ok(new ResponseBody(
                productService.getProductDetail(Long.parseLong(productID)),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

    @GetMapping("/Product/GetObjByCode/{id}")
    public ResponseEntity getObj(@PathVariable(name = "id") String productID) {
        return ResponseEntity.ok(new ResponseBody(
                productService.getProductDetail(Long.parseLong(productID)),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

    @PostMapping("/Product/GetFilterPaging")
    public ResponseEntity getFilter(@RequestBody FilterParam param) {
        return ResponseEntity.ok(new ResponseBody(
                productService.productFilter(param),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }
    @PostMapping("/Product/ProductSold")
    public ResponseEntity productSold(@RequestBody FilterParam param) {
        return ResponseEntity.ok(new ResponseBody(
                productService.productSold(param),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }
    @PostMapping("/Product/View")
    public ResponseEntity productView(@RequestBody FilterParam param) {
        return ResponseEntity.ok(new ResponseBody(
                productService.updateView(param.getUserID(), param.getParentID()),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

}
