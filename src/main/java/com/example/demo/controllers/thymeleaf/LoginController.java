package com.example.demo.controllers.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String showLogin() {
        return "sign-in";
    }

    @GetMapping("/auth")
    public String authenticate() {
        return "redirect:/";
    }

    @GetMapping("/index")
    public String showAccessDenied() {
        return "index";
    }
}
