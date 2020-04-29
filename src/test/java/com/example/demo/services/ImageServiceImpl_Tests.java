package com.example.demo.services;

import com.example.demo.models.Picture;
import com.example.demo.models.User;
import com.example.demo.repositories.PictureRepository;
import com.example.demo.repositories.PostRepository;
import com.example.demo.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.example.demo.Factory.createPicture;
import static com.example.demo.Factory.createUser;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImpl_Tests {

    @Mock
    UserRepository repository;

    @Mock
    PostRepository mockPostRepository;

    @Mock
    PictureRepository pictureRepository;

    @InjectMocks
    ImageServiceImpl imageService;


    @Test
    public void saveUserPhotoShouldSavePicture() {
        User user = createUser();


        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return "fileName";
            }

            @Override
            public String getOriginalFilename() {
                return "originalName";
            }

            @Override
            public String getContentType() {
                return "jpg";
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
                return "Picture bytes".getBytes();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
        Picture picture = new Picture();
        Byte [] byteArr = new Byte[(int) file.getSize()];
        picture.setData(byteArr);
        user.setPhoto(picture);
        Mockito.when(repository.getById(anyLong())).thenReturn(user);
        imageService.saveUserPhoto(user.getId(), byteArr);

        Mockito.verify(repository, times(1)).save(user);
    }


    @Test
    public void saveCoverPhoto_ShouldSave_CoverPhoto(){
        User user = createUser();

        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return "fileName";
            }

            @Override
            public String getOriginalFilename() {
                return "originalName";
            }

            @Override
            public String getContentType() {
                return "jpg";
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
                return "Picture bytes".getBytes();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };

        Byte [] byteArr = new Byte[(int) file.getSize()];
        user.setCoverPhoto(byteArr);
        Mockito.when(repository.getById(anyLong())).thenReturn(user);
        imageService.saveUserCover(user.getId(), byteArr);

        Mockito.verify(repository, times(1)).save(user);
    }

//    @Test(expected = NullPointerException.class)
//    public void savePostPhotoShouldThrow_When_PictureIsNull(){
//        Post post = createPost();
//
//        MultipartFile file = new MultipartFile() {
//            @Override
//            public String getName() {
//                return "fileName";
//            }
//
//            @Override
//            public String getOriginalFilename() {
//                return "originalName";
//            }
//
//            @Override
//            public String getContentType() {
//                return "jpg";
//            }
//
//            @Override
//            public boolean isEmpty() {
//                return false;
//            }
//
//            @Override
//            public long getSize() {
//                return 0;
//            }
//
//            @Override
//            public byte[] getBytes() throws IOException {
//                return "Picture bytes".getBytes();
//            }
//
//            @Override
//            public InputStream getInputStream() throws IOException {
//                return null;
//            }
//
//            @Override
//            public void transferTo(File dest) throws IOException, IllegalStateException {
//
//            }
//        };
//
//        imageService.savePostPhoto(post.getId(), file);
//
//        Mockito.verify(mockPostRepository, times(1)).save(post);
//    }

    @Test
    public void setPrivacyShould_SetPrivacy() throws IOException {
        User user = createUser();
        Picture picture = createPicture();
        user.setPhoto(picture);

        imageService.setPrivacy(picture,true);

        Assert.assertSame(user.getPhoto().isPublic(), true);
    }
}
