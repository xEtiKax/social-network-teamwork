package com.example.demo.services.interfaces;

import com.example.demo.models.Picture;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void savePostPhoto(long postId, MultipartFile file);

    void saveUserPhoto(long userId, MultipartFile file);

    void saveUserCover(long userId, MultipartFile file);

    void setPrivacy(Picture picture, boolean isPublic);
;}
