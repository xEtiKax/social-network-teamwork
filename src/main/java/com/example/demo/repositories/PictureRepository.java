package com.example.demo.repositories;

import com.example.demo.models.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface PictureRepository extends JpaRepository<Picture, Serializable> {

    Picture findById(int id);
}
