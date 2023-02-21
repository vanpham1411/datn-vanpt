package com.example.datn.controller;

import com.example.datn.domain.entity.Category;
import com.example.datn.domain.utility.FilterParam;
import com.example.datn.service.CategoryService;
import com.example.datn.service.LayoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class CategoryController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    LayoutService layoutService;

    @GetMapping("{name}/GetLayoutConfig")
    public ResponseEntity getLayout(@PathVariable(name = "name") String name) {
        return ResponseEntity.ok(new ResponseBody(
                layoutService.getConfig(name),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }
    @PostMapping(path = "/Category")
    public ResponseEntity createCategory(@RequestBody Category category) {
        return ResponseEntity.ok(new ResponseBody(
                categoryService.saveCategory(category),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }
    @PutMapping(path = "/Category")
    public ResponseEntity updateCategory(@RequestBody Category category) {
        return ResponseEntity.ok(new ResponseBody(
                categoryService.saveCategory(category),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }
    @GetMapping(path = "/Category/GetPopularCategories")
    public ResponseEntity getList() {
        List<Category> categories = categoryService.getAllCategory();
        return ResponseEntity.ok(new ResponseBody(
                categoryService.getAllCategory(),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

    @PostMapping(path = "/Category/GetFilterPaging")
    public ResponseEntity getFilterPaging(@RequestBody FilterParam param) {
        return ResponseEntity.ok(new ResponseBody(
                categoryService.getFilter(param),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }
    @DeleteMapping(path = "/Category")
    public ResponseEntity delete(long categoryID) {
        return ResponseEntity.ok(new ResponseBody(
                categoryService.deleteCategory(categoryID),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

    @PostMapping(path = "/Category/Delete")
    public ResponseEntity delete(List<Long> categoryIDList) {
        return ResponseEntity.ok(new ResponseBody(
                categoryService.deleteMulti(categoryIDList),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

    @GetMapping(path = "/Category/{id}")
        public ResponseEntity getByCode(@PathVariable("id") String id) {
            return ResponseEntity.ok(new ResponseBody(
                categoryService.getByCode(Long.parseLong(id)),
                ResponseBody.Status.SUCCESS,
                ResponseBody.Code.SUCCESS
        ));
    }

}
