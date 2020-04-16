package com.example.demo.utils;

import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

@Component
public class Mapper {

    @Autowired
    public Mapper() {

    }

    public static User userDTOtoUserMapper(UserDTO userDTO) throws IOException {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEnabled(1);
        user.setPhoto(generateDefaultUserPhoto());
        user.setCoverPhoto(generateDefaultCoverPhoto());
        user.setPassword(userDTO.getPassword());
        return user;
    }

    public static Byte[] generateDefaultCoverPhoto() throws IOException {

        URL url = new URL("https://atiinc.org/wp-content/uploads/2017/01/cover-default.jpg?fbclid=IwAR0HYPjoqiQbHkrYUD0rYbX6AMr0m6Tpc3FazFCdfeiU5i-LAHWl40JTW88");
        BufferedImage bImage = ImageIO.read(url);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);

        byte[] pictureBytes = bos.toByteArray();
        Byte[] byteObjects = new Byte[pictureBytes.length];
        int i=0;
        for(byte b: pictureBytes)
            byteObjects[i++] = b;

        return byteObjects;
    }

    public static Byte[] generateDefaultUserPhoto() throws IOException {

        URL url = new URL("https://genslerzudansdentistry.com/wp-content/uploads/2015/11/anonymous-user.png");
        BufferedImage bImage = ImageIO.read(url);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", bos);

        byte[] pictureBytes = bos.toByteArray();
        Byte[] byteObjects = new Byte[pictureBytes.length];
        int i=0;
        for(byte b: pictureBytes)
            byteObjects[i++] = b;

        return byteObjects;
    }
}
