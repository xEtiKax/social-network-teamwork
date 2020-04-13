package com.example.demo.services;

import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
public class ImageServiceImpl implements ImageService {

    private PostRepository postRepository;
    private UserRepository userRepository;

    public ImageServiceImpl(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void saveUserPhoto(long userId, MultipartFile file) {
        try {
            User user = userRepository.findById(userId).get();

            Byte[] byteObjects = multiPartToByteArr(file);
            user.setPhoto(byteObjects);
            userRepository.save(user);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public void savePostPhoto(long postId, MultipartFile file) {
        try {
            Post post = postRepository.getById(postId);

            Byte[] byteObjects = multiPartToByteArr(file);
            post.setPicture(byteObjects);
            postRepository.save(post);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private Byte[] multiPartToByteArr(MultipartFile file) throws IOException {
        Byte[] byteObjects = new Byte[file.getBytes().length];
        int i = 0;
        for (byte b : file.getBytes()) {
            byteObjects[i++] = b;
        }
        return byteObjects;
    }
}
