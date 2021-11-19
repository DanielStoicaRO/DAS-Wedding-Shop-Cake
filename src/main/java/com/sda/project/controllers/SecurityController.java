package com.sda.project.controllers;

import com.sda.project.entities.AuthorityEntity;
import com.sda.project.entities.UserEntity;
import com.sda.project.models.RegisterModel;
import com.sda.project.repositories.AuthorityRepository;
import com.sda.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class SecurityController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public ModelAndView getLogin() {
        ModelAndView modelAndView = new ModelAndView("login");
        return modelAndView;
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        ModelAndView modelAndView = new ModelAndView("register");
        modelAndView.addObject("registerContainer", new RegisterModel());
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView registerUser(@ModelAttribute("registerContainer") RegisterModel registerModel) {
        ModelAndView modelAndView = new ModelAndView("redirect:/login");

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(registerModel.getUsername());
        userEntity.setPassword(passwordEncoder.encode(registerModel.getPassword()));
        userEntity.setCity(registerModel.getCity());
        userEntity.setAddress(registerModel.getAddress());
        userEntity.setEmailAddress(registerModel.getEmailAddress());

        userEntity.setEnabled(true);
        userRepository.save(userEntity);

        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setUsername(registerModel.getUsername());
        authorityEntity.setAuthority("USER");
        authorityRepository.save(authorityEntity);

        return modelAndView;
    }

}
