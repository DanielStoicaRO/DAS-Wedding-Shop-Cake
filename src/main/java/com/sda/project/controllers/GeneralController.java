package com.sda.project.controllers;

import com.sda.project.repositories.CartRepository;
import com.sda.project.repositories.ProductRepository;
import com.sda.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


import java.util.Optional;

@Controller
public class GeneralController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/index")
    public ModelAndView getIndex(String keyword) {
        ModelAndView modelAndView = new ModelAndView("index");

        Optional<User> user = getLoggedInUser();
        if (user.isPresent()) {
//            cart count
            Integer userId = userRepository.findUserEntityByUsername(user.get().getUsername()).getUserId();
            Long cartLength = cartRepository.countAllByUserId(userId);
            modelAndView.addObject("cartSize", cartLength);
        }
        //        search
        if (keyword != null) {
            modelAndView.addObject("stockList", productRepository.findByKeyword(keyword));
        } else {
            modelAndView.addObject("stockList", productRepository.findAll());
            return modelAndView;
        }
        return modelAndView;
    }

    @GetMapping("/about")
    public ModelAndView getAbout() {
        ModelAndView modelAndView = new ModelAndView("about");
        return modelAndView;
    }

    @GetMapping("/stuff")
    public ModelAndView getServices() {
        ModelAndView modelAndView = new ModelAndView("stuff");
        return modelAndView;
    }

    @GetMapping("/offers")
    public ModelAndView getOffers() {
        ModelAndView modelAndView = new ModelAndView("offers");
        return modelAndView;
    }
    @GetMapping("/gallery")
    public ModelAndView getGallery() {
        ModelAndView modelAndView = new ModelAndView("gallery");
        return modelAndView;
    }

    //                cart count / userIsPresent
    public Optional<User> getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (null != auth && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @GetMapping("/contact")
    public ModelAndView getContact() {
        ModelAndView modelAndView = new ModelAndView("contact");
        return modelAndView;
    }

    @GetMapping("/cart")
    public ModelAndView getCart() {
        ModelAndView modelAndView = new ModelAndView("cart");
        return modelAndView;
    }

    @GetMapping("/pay")
    public ModelAndView getPay() {
        ModelAndView modelAndView = new ModelAndView("pay");
        return modelAndView;
    }



}
