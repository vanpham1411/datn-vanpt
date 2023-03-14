package com.example.datn;

import com.example.datn.domain.entity.Item;
import com.example.datn.domain.entity.Product;
import com.example.datn.domain.repository.ItemRepository;
import com.example.datn.domain.repository.ProductESRepository;
import com.example.datn.domain.repository.ProductRepository;
import com.example.datn.domain.repository.SuggestUserRepository;
import com.example.datn.domain.utility.DataResponse;
import com.example.datn.domain.utility.SequenceGenerator;
import com.example.datn.service.ProductService;
import com.example.datn.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
@Slf4j
public class DatnApplication implements CommandLineRunner  {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductESRepository productESRepository;
    @Autowired
    SuggestUserRepository suggestUserRepository;

    @Autowired
    ProductService productService;
    public static void main(String[] args) {
        SpringApplication.run(DatnApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/v1").allowedOrigins("http://localhost:8080");
            }
        };
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("get id: " +SequenceGenerator.getInstance().nextId());
//        List<Long> longs = new ArrayList<>();
//        longs.add(3L);
//        longs.add(4L);
//        suggestUserRepository.updateDoc(1, longs);
//        DataResponse dataResponse = productESRepository.search("ao", -1,0,50);
//        List<Long> listInputID = new ArrayList<>();
//        listInputID.add(12818737659L);
//        listInputID.add(13522827723L);
//        List<Long> list = suggestUserRepository.suggestProduct(listInputID);
//        log.info("get suggestion: {}",list);

//        for(int i = 0; i< 20; i++) {
//            log.info("get:{}",SequenceGenerator.getInstance().nextId());
//
//        }

//        sync data
//        productService.syncDataToES();
    }
}
