package com.example.demo.services;

import com.example.demo.models.Post;
import com.example.demo.models.User;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.example.demo.Factory.createPost;
import static com.example.demo.Factory.createUser;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ImageServiceImpl_Tests {

    @Mock
    UserRepository repository;

    @Mock
    PostRepository mockPostRepository;

    @InjectMocks
    ImageServiceImpl imageService;


    @Test
    public void updateUser_ShouldUpdateUsername() {
        User user = createUser();
        when(repository.existsById(anyInt())).thenReturn(true);
        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };

        Assert.assertThrows(NullPointerException.class,() -> imageService.saveUserPhoto(user.getId(),file));

    }

    @Test
    public void updateBeer_ShouldUpdatePostPhoto() {
        Post post = createPost();
        when(mockPostRepository.existsById(anyInt())).thenReturn(true);
        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };

        Assert.assertThrows(NullPointerException.class,() -> imageService.savePostPhoto(post.getId(),file));

    }
}
