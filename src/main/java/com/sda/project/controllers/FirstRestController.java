package com.sda.project.controllers;

import com.sda.project.entities.ProductEntity;
import com.sda.project.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FirstRestController {

    public FirstRestController(ProductRepository productRepository) {
        System.out.println(getClass().getSimpleName() + " created");
    }

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/api/products")
    public List<ProductEntity> getProducts() {
        return productRepository.findAll();
    }

    @PostMapping("/api/saveProduct")
    public String saveProduct(@RequestBody @Valid ProductEntity productEntity, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return bindingResult.getFieldErrors().stream().map(e -> e.getDefaultMessage()).collect(Collectors.joining("; "));
        }
        productRepository.save(productEntity);
        return "Success!";
    }

    @PutMapping("/api/modifyCar/{id}")
    public String editCarNew(@RequestBody ProductEntity productEntity,
                             @PathVariable Integer id) {
        ProductEntity dbEntity = productRepository.findById(id).orElse(new ProductEntity());
        dbEntity.setBrand(productEntity.getBrand());
        dbEntity.setKg(productEntity.getKg());
        dbEntity.setPrice(productEntity.getPrice());
        dbEntity.setQuantity(productEntity.getQuantity());
        productRepository.save(dbEntity);
        return "Successfully modify";
    }
}
