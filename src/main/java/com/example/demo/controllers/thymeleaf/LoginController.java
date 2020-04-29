package com.example.demo.controllers.thymeleaf;

import com.example.demo.models.DTO.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String showLogin(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "sign-in";
    }

    @GetMapping("/index")
    public String showAccessDenied() {
        return "index";
    }
}

