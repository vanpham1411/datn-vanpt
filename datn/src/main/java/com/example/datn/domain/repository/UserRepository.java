package com.example.datn.domain.repository;

import com.example.datn.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select count(u.userID) from User u where u.name = ?1")
    public int checkExistByName(String userName);

    @Query("select u from User u where u.name = ?1 and u.password = ?2")
    public User login(String name, String password);

    @Query(value = "update user set name = ?1, mobile = ?2, address=?3 where user_id = ?4", nativeQuery = true)
    @Modifying
    public int updateUser(String name, String mobile, String address, long userID);


}
