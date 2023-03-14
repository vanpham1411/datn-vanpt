package com.example.datn.domain.repository;

import com.example.datn.domain.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Item,Long> {
    @Query("select i from Item i where i.productID = ?1")
    List<Item> getAllItemOfProductID(Long productId);


//    @Query(value = "delete from item where ")
    @Modifying
    @Query(value = "delete from item i where i.product_id= ?1 ", nativeQuery = true)
    int deleteAllByProduct(long productId);
    @Query("select  item.productID from  Item item where item.itemID = ?1")
    long getProductID(long itemID);

    @Query("select item from Item item where item.itemID = ?1 and item.quantity > ?2")
    Item getAvaiableItem(long itemID, int quantity);
    @Query("select distinct i.typeID from Item i where i.productID = ?1 ")
    List<Long> getTypeOfProduct(long productID);

    @Query(value = "update item set item.quantity  = item.quantity - :amount where item.item_id = :itemID ", nativeQuery = true)
    @Modifying
    int updateQuantity(@Param("amount")int quantity, @Param("itemID") long itemID);

    @Query(value = "select i from Item i where i.itemID = ?1 and  i.quantity >?2" )
    Item checkQuantity(long itemID, int quantity);

    @Query(value = "update item set item.quantity  = item.quantity + :amount where item.item_id = :itemID", nativeQuery = true)
    @Modifying
    int addQuantity(@Param("amount")int quantity, @Param("itemID") long itemID);

}

