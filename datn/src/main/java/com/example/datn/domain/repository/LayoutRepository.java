package com.example.datn.domain.repository;

import com.example.datn.domain.entity.LayoutConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LayoutRepository extends JpaRepository<LayoutConfig, Integer> {
    LayoutConfig findByLayoutName(String layoutName);

}
