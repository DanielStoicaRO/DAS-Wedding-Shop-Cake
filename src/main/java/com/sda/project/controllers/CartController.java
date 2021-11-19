package com.sda.project.controllers;

import com.sda.project.entities.CartEntity;
import com.sda.project.entities.ProductEntity;
import com.sda.project.repositories.CartRepository;
import com.sda.project.repositories.ProductRepository;
import com.sda.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class CartController {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    public CartController() {
        System.out.println(this.getClass().getSimpleName() + " created");
    }

    public Optional<User> getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (null != auth && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @GetMapping("/get-cart")
    public ModelAndView getCart() {
        ModelAndView modelAndView = new ModelAndView("/cart");
        Optional<User> user = getLoggedInUser();
        if (user.isPresent()) {
            modelAndView.addObject("cart", cartRepository.findAllByUser_Username(user.get().getUsername()));
//cartCount
            Integer userId = userRepository.findUserEntityByUsername(user.get().getUsername()).getUserId();
            Long cartLength = cartRepository.countAllByUserId(userId);
            modelAndView.addObject("cartSize", cartLength);
            CartEntity dbCartEntity = cartRepository.findByProductIdAndUserId(userId, userId);
//total price
            Double totalCart = 0.0;
            List<CartEntity> carts = cartRepository.findAllByUser_Username(user.get().getUsername());
            for (CartEntity item : carts) {
                totalCart += item.getTotal();
            }
            modelAndView.addObject("cart", carts);
            modelAndView.addObject("total", totalCart);
        } else {
            modelAndView.addObject("cart", new ArrayList<>());
        }
        return modelAndView;
    }

    @GetMapping("/delete-cart/{id}")
    public ModelAndView deleteCard(@PathVariable Integer id) {
        Optional<CartEntity> cartEntity = cartRepository.findById(id);
        if (cartEntity.isPresent()) {
            cartRepository.delete(cartEntity.get());
            ModelAndView modelAndView = new ModelAndView("redirect:/get-cart");
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/errorHtml");
        return modelAndView;
    }

    @GetMapping("/add-cart/{id}")
    public ModelAndView addCard(@PathVariable Integer id, CartEntity cartEntity) {
        ModelAndView modelAndView = new ModelAndView("redirect:/cards");

        Optional<User> user = getLoggedInUser();

        if (user.isPresent()) {
            Integer userId = userRepository.findUserEntityByUsername(user.get().getUsername()).getUserId();
            CartEntity dbCartEntity = cartRepository.findByProductIdAndUserId(id, userId);
            if (dbCartEntity != null) {

                dbCartEntity.setQuantity(dbCartEntity.getQuantity() + 1);
                cartRepository.save(dbCartEntity);
            } else {
                cartEntity.setQuantity(1);
                cartEntity.setProductId(id);
                cartEntity.setUserId(userRepository.findByUsername(user.get().getUsername()).getUserId());
                cartRepository.save(cartEntity);
            }
        }

        return modelAndView;
    }

    //delete from cart
    @GetMapping("/successfully")
    public ModelAndView orderSuccessfully(CartEntity cart) {
        ModelAndView modelAndView = new ModelAndView("/successfully");
        Optional<User> user = getLoggedInUser();

        if (user.isPresent()) {
            List<CartEntity> carts = cartRepository.findAllByUser_Username(user.get().getUsername());

            if (!carts.isEmpty()) {
                for (CartEntity item : carts) {
                    Integer prodQuantityUntilOrder = productRepository.getById(item.getProductId()).getQuantity();
                    ProductEntity product = productRepository.getById(item.getProductId());
                    product.setQuantity(prodQuantityUntilOrder - item.getQuantity());
                    productRepository.save(product);
                    cartRepository.delete(item);
                }
            }
        }
        return modelAndView;
    }

}
