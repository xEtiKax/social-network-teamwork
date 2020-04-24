package com.example.demo.repositories;

import com.example.demo.models.Picture;
import com.example.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;

public interface PictureRepository extends JpaRepository<Picture, Serializable> {

    @Query("SELECT p FROM Picture p WHERE p.id =?1")
    Picture getById(int id);
}
