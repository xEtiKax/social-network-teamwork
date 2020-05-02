package com.example.demo.services;

import com.example.demo.models.Picture;
import com.example.demo.models.User;
import com.example.demo.repositories.PictureRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.interfaces.ImageService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
}
