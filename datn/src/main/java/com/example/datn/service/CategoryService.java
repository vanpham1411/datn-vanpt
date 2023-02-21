package com.example.datn.service;

import com.example.datn.domain.entity.Category;
import com.example.datn.domain.repository.CategoryRepository;
import com.example.datn.domain.utility.DataResponse;
import com.example.datn.domain.utility.FilterParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        return categoryRepository.findAll();
    }

    public DataResponse getFilter(FilterParam param) {
        List<Category> categories = new ArrayList<>();
        if(param.getKeyword() ==  null)
            categories = categoryRepository.findAllByIsDeleted(0);
        else categories = categoryRepository.filterByKeyword(param.getKeyword());
        DataResponse data = DataResponse.builder()
                .data((categories))
                .totalPage(1)
                .totalRecord(categories.size())
                .build();
        return data;
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }
    public int deleteCategory(long categoryID) {
       return categoryRepository.updateDeleted(1,categoryID);

    }

    public int deleteMulti(List<Long> list) {
        list.forEach(category -> {
            categoryRepository.updateDeleted(1,category);

        });
        return 1;
    }

    public Category getByCode(long id) {
        return categoryRepository.findById(id).get();
    }

}
