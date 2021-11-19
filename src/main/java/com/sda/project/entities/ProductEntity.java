package com.sda.project.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Getter
@Setter
@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productId;
    private String brand;
    private String kg;
    private Integer price;
    private Integer quantity;
    private String description;
    private String imagineUrl;


    @ManyToOne
    @JoinColumn(name = "categoryId")
    private CategoryEntity category;



}
