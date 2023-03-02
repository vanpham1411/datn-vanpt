package com.example.datn.domain.repository;

import com.example.datn.domain.entity.Product;
import com.example.datn.domain.model.ProductRevenue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductCode(String productCode);
    @Query(value = "select * from product p where p.status = 0 limit ?1 offset ?2 ", nativeQuery = true)
    List<Product> getProductInfo(int limit, int offset);
    @Modifying
    @Query(value = "update product set status = -1 where product_id = ?1", nativeQuery = true)
    int removeProduct(long productID);

    @Modifying
    @Query(value = "update product set status = -1 where category_id = ?1", nativeQuery = true)
    int deleteInCategory(long categoryID);

    @Modifying
    @Query(value = "update product set sold_nb = sold_nb + ?2  where product_id = ?1", nativeQuery = true)
    int updateSoldNb(long productID, int quantity);

    @Query(value = "select p from Product p where p.name like %?1% or p.categoryID = ?2")
    List<Product> filterProductByCategory(String keyword, long categoryID);
    @Query(value = "select p from Product p where p.name like %?1%")
    List<Product> filterProduct(String keyword);

    @Query(value = "SELECT p.product_id as productID, p.name as name, sum(oi.quantity) as quantity, p.cost as amount FROM product_order as o inner join order_item as oi on o.order_id  = oi.order_id inner join item i on i.item_id = oi.item_id inner join product as p on p.product_id = i.product_id where o.status = 4 and p.status =0 and o.create_time >= ?1 and o.create_time <= ?2 group by p.product_id, p.name, p.cost order by quantity desc ", nativeQuery = true)
    List<List<Object>> getPopularProduct(Timestamp startDate, Timestamp endDate);

//    @Query(value = "SELECT item.product_id FROM order_item as i inner join product_order as o on i.order_id = o.order_id  inner join item as item on i.item_id = item.item_id where  o.user_id = ?1 order by o.order_id desc limit ?2", nativeQuery = true)
//    List<Long> getRecentProduct(long userID,int limit);

}
