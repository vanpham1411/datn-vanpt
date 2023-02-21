package com.example.datn.service;

import com.example.datn.domain.entity.LayoutConfig;
import com.example.datn.domain.repository.LayoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LayoutService {
    @Autowired
    LayoutRepository layoutRepository;

    public LayoutConfig getConfig(String name) {
        return layoutRepository.findByLayoutName(name);
    }
}
