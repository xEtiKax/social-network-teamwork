package com.example.demo.services;

import com.example.demo.models.Picture;
import com.example.demo.models.User;
import com.example.demo.repositories.PictureRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.ImageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@Transactional
public class ImageServiceImpl implements ImageService {

    private UserRepository userRepository;
    private PictureRepository pictureRepository;

    public ImageServiceImpl(UserRepository userRepository, PictureRepository pictureRepository) {
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public void saveUserPhoto(long userId, Byte[] file) {
        User user = userRepository.getById(userId);
        user.getPhoto().setData(file);
        userRepository.save(user);
    }

    @Override
    public void saveUserCover(long userId, Byte[] file) {
        User user = userRepository.getById(userId);
        user.setCoverPhoto(file);
        userRepository.save(user);
    }

    @Override
    public void setPrivacy(Picture picture, boolean isPublic) {
        picture.setPublic(isPublic);
        pictureRepository.save(picture);
    }

    public void renderImage(HttpServletResponse response, Byte[] photo) throws IOException {
        byte[] byteArray = new byte[photo.length];

        int i = 0;

        for (Byte wrappedByte : photo) {
            byteArray[i++] = wrappedByte;
        }
        response.setContentType("image/jpeg");
        InputStream is = new ByteArrayInputStream(byteArray);
        IOUtils.copy(is, response.getOutputStream());
    }
}
