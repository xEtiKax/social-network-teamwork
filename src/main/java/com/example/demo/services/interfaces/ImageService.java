package com.example.demo.services.interfaces;

import com.example.demo.models.Picture;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ImageService {

    void saveUserPhoto(long userId, Byte[] file);

    void saveUserCover(long userId, Byte[] file);

    void setPrivacy(Picture picture, boolean isPublic);

    void renderImage(HttpServletResponse response, Byte[] photo) throws IOException;

}
