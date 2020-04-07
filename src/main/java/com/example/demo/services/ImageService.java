package com.example.demo.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void savePostPhoto(int postId, MultipartFile file);

    void saveUserPhoto(int userId, MultipartFile file);
}
