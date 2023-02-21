package com.example.datn.domain.repository;

import com.example.datn.domain.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByUserID(long userID);
    List<CartItem> deleteAllByUserID(long userID);

}
