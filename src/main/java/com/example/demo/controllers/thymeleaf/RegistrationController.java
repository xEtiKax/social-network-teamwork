package com.example.demo.controllers.thymeleaf;

import com.example.demo.models.DTO.UserDTO;
import com.example.demo.models.User;
import com.example.demo.services.UserService;
import com.example.demo.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class RegistrationController {
    private UserDetailsManager userDetailsManager;
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @Autowired
    public RegistrationController(UserDetailsManager userDetailsManager , PasswordEncoder passwordEncoder, UserService userService) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        model.addAttribute("isRegister",true);
        return "sign-in";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            model.addAttribute("error", "Username/password can't be empty!");
            return "sign-in";
        }
        User user = Mapper.userDTOtoUserMapper(userDTO);

        model.addAttribute("user",userDTO);
        if (userDetailsManager.userExists(user.getUsername())) {
            model.addAttribute("error", "User with the same username already exists!");
            return "sign-in";
        }

        if (!userDTO.getPassword().equals(userDTO.getPasswordConfirmation())) {
            model.addAttribute("errorPass", "Password does't match!");
            return "sign-in";
        }

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
        org.springframework.security.core.userdetails.User newUser =
                new org.springframework.security.core.userdetails.User(user.getUsername(), passwordEncoder.encode(user.getPassword()), authorities);
        userDetailsManager.createUser(newUser);
        model.addAttribute("successful","You registered successfully");

        return "sign-in";
    }

    @GetMapping("/register-confirmation")
    public String showRegisterConfirmation() {
        return "register-confirmation";
    }
}