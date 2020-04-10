package com.example.demo.controllers.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/admin")
    public String showAdminPage() {
        return "admin";
    }
}