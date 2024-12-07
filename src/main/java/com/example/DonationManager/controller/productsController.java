package com.example.DonationManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class productsController {

    @GetMapping("/products")
    public String getProductsPage() {
        return "products";
    }

    @GetMapping("/products/{id}")
    public String getProductDetailsPage(@PathVariable Integer id) {
        return "product";
    }
    
    
}
