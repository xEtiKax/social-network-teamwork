package com.example.demo.repositories;

import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.io.Serializable;


public interface UserRepository extends JpaRepository<User, Serializable> {

    @Query("SELECT u FROM User u where u.username = ?1")
    User getUserByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.id =?1")
    User getById(int id);

    @Query("SELECT u FROM User u WHERE u.username =?1")
    User getUserByEmail(String email);

    @Transactional
    @Query("UPDATE User u set u.enabled = 0 where u.id = ?1")
    @Modifying
    void deleteUser(int id);
}
