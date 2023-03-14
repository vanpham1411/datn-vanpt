package com.example.datn.domain.repository;

import com.example.datn.domain.entity.Category;
import com.example.datn.domain.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeRepository extends JpaRepository<Type,Long> {

    @Query("select t from Type t where t.typeID < 1000000000000")
    List<Type> findType();

}
