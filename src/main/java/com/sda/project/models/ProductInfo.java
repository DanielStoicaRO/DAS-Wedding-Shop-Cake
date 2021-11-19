package com.sda.project.models;

import lombok.Value;

@Value
public class ProductInfo {

    Integer productId;
    String brand;
    String kg;
    Integer price;
    Integer quantity;
    String description;
    String imagineUrl;

}
