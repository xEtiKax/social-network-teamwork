package com.example.demo.services.interfaces;

import com.example.demo.models.Picture;

public interface ImageService {

    void saveUserPhoto(long userId, Byte[] file);

    void saveUserCover(long userId, Byte[] file);

    void setPrivacy(Picture picture, boolean isPublic);

    ;
}
