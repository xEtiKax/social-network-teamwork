package com.example.demo.services;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void savePostPhoto(long postId, MultipartFile file);

    void saveUserPhoto(long userId, MultipartFile file);
}
