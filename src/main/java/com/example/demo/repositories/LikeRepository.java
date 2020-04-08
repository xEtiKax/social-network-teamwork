package com.example.demo.repositories;

import com.example.demo.models.Like;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

public interface LikeRepository extends JpaRepository<Like, Serializable> {

    @Query("SELECT l FROM Like l where l.id = ?1")
    Like getLikeByUserId(int userId);

}
