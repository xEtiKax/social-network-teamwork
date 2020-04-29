package com.example.demo.controllers.thymeleaf;

import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.models.User;
import com.example.demo.services.interfaces.ForgottenPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ForgottenPasswordController {

    private ForgottenPasswordService forgottenPasswordService;

    @Autowired
    public ForgottenPasswordController(ForgottenPasswordService forgottenPasswordService) {
        this.forgottenPasswordService = forgottenPasswordService;
    }

    @GetMapping("/password/forgotten")
    public String showForgottenPasswordForm() {
        return "forgotten-password";
    }

    @PostMapping("/password/forgotten")
    public String forgottenPasswordSend(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorEmail","Wrong email");
            return "forgotten-password";
        }
        if (!forgottenPasswordService.userValidation(user.getUsername(),user.getEmail())) {
            model.addAttribute("error","Details doesn't match");
            return "forgotten-password";
        }
        try {
            forgottenPasswordService.sendEmailForPassword(user.getUsername());
        }catch (EntityNotFoundException e) {
            model.addAttribute("error",e.getMessage());
            return "forgotten-password";
        }
        model.addAttribute("success","New password was sent!");
        return "forgotten-password";
    }
}
