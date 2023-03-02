package com.example.datn.domain.repository;

import com.example.datn.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select o from Order o where o.status=?1")
    List<Order> getAllByStatus(int status);

    @Query("select o from Order o where o.userID = ?1 and o.status = ?2 order by o.createTime desc")
    List<Order> filterUser(long userID, int status);
    @Query(value = "select * from product_order", nativeQuery = true)
    List<Order> filterOrder( int limitValue,int offset);

    @Query(value = "select * from product_order o where create_time > :minDate and create_time <= :maxDate and (user_name like %:keyword%) order by create_time desc limit :limitValue offset :offsetValue ", nativeQuery = true)
    List<Order> filter(@Param("minDate") Timestamp minDate,@Param("maxDate") Timestamp maxDate, @Param("keyword") String keyword, @Param("limitValue") int limitValue, @Param("offsetValue") int offset);

    @Query(value = "select * from product_order o where create_time >= :minDate and create_time <= :maxDate and (user_name like %:keyword%) and o.status= :status order by create_time desc limit :limitValue offset :offsetValue ", nativeQuery = true)
    List<Order> filterStatus(@Param("minDate") Timestamp minDate,@Param("maxDate") Timestamp maxDate,@Param("keyword") String keyword, @Param("limitValue") int limitValue, @Param("offsetValue") int offset,@Param("status") int status);

    @Query(value = "update product_order set status = ?1 where order_id = ?2", nativeQuery = true)
    @Modifying
    int updateStatus(int status, long orderID);

}
