package com.example.datn.domain.repository;

import com.example.datn.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CategoryRepository extends JpaRepository<Category,Long> {
    @Query("update Category set isDeleted = ?1 where categoryId = ?2")
    @Modifying
    public int updateDeleted(int status, Long categoryID);
    List<Category> findAllByIsDeleted(int deleted);
    @Query("select c from Category c where (c.name like %?1% or c.code like %?1%) and c.isDeleted = 0")
    public List<Category> filterByKeyword(String keyword);

}
