package com.example.datn.domain.repository;

import com.example.datn.domain.entity.ViewProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewProductRepository extends JpaRepository<ViewProduct,Long> {
    @Query("select v.productID from ViewProduct v where v.userID = ?1 order by v.timestamp desc ")
    Long getRecentView(long userID);
}
