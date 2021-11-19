package com.sda.project.service;

import com.sda.project.entities.ProductEntity;
import com.sda.project.repositories.CartRepository;
import com.sda.project.repositories.ProductRepository;
import com.sda.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Controller
public class ProductServiceController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;


    @GetMapping("/cards")
    public ModelAndView getCardsPage(String keyword) {
        ModelAndView modelAndView = new ModelAndView("cards");

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

    //            cart count / userIsPresent
    public Optional<User> getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (null != auth && auth.getPrincipal() instanceof User) {
            User user = (User) auth.getPrincipal();
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @GetMapping("/product/add")
    public ModelAndView productAdd() {
        ModelAndView modelAndView = new ModelAndView("productEdit");
        modelAndView.addObject("product", new ProductEntity());
        return modelAndView;
    }

    @PostMapping(value = "/product/save", consumes = {"multipart/form-data"})
    public ModelAndView productSave(@ModelAttribute("product") ProductEntity productEntity, @RequestParam("file") MultipartFile file) throws IOException {
        ModelAndView modelAndView = new ModelAndView("redirect:/cards");
        String path1 = "target/classes/static/imagines";
        String path2 = "src/main/resources/static/imagines";
        String filename = file.getOriginalFilename();

        if (filename != null && !filename.isEmpty()) {
            saveFile(path1, filename, file);
            saveFile(path2, filename, file);
            productEntity.setImagineUrl("/imagines/" + filename);
        }
        productRepository.save(productEntity);
        return modelAndView;
    }

    public void saveFile(String path, String fileName, MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(path);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    @GetMapping("/product/view/{id}")
    public ModelAndView productView(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("productView");
        modelAndView.addObject("product", productRepository.getById(id));

        Optional<User> user = getLoggedInUser();
        if (user.isPresent()) {
//            cart count
            Integer userId = userRepository.findUserEntityByUsername(user.get().getUsername()).getUserId();
            Long cartLength = cartRepository.countAllByUserId(userId);
            modelAndView.addObject("cartSize", cartLength);
        }

        return modelAndView;
    }

    @GetMapping("/product/edit/{id}")
    public ModelAndView productEdit(@PathVariable Integer id) {
        ModelAndView modelAndView = new ModelAndView("productEdit");
        modelAndView.addObject("product", productRepository.getById(id));
        return modelAndView;
    }

    @GetMapping("/product/delete/{id}")
    public ModelAndView productDelete(@PathVariable Integer id) {
        Optional<ProductEntity> productEntity = productRepository.findById(id);
        if (productEntity.isPresent()) {
            productRepository.delete(productEntity.get());
            ModelAndView modelAndView = new ModelAndView("redirect:/cards");
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("error");
        return modelAndView;
    }
}
