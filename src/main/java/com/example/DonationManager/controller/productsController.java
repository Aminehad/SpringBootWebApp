package com.example.DonationManager.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.example.DonationManager.model.Category;
import com.example.DonationManager.model.Item;
import com.example.DonationManager.model.enumeration.ProductDelivery;
import com.example.DonationManager.model.enumeration.ProductStatus;
import com.example.DonationManager.repository.CategoryRepository;
import com.example.DonationManager.repository.ProductRepository;
import com.example.services.ProductService;

import org.springframework.ui.Model;


@Controller
public class productsController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;


    @GetMapping("/products")
    public String getProductsPage(
            @RequestParam(name = "category", required = false) Long categoryId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "delivery", required = false) String delivery,
            @RequestParam(name = "keywords", required = false) String keywords,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "orderDirection", required = false) String orderDirection,
            @RequestParam(name = "orderBy", required = false) Integer orderBy,
            Model model) {

        ProductDelivery productDelivery = null;
        ProductStatus productStatus = null;

        if (ProductService.isValidProductDelivery(delivery)) {
            productDelivery = ProductDelivery.valueOf(delivery.toUpperCase());
        }
        if (ProductService.isValidProductStatus(status)) {
            productStatus = ProductStatus.valueOf(status.toUpperCase());
        }

        List<Item> products = productRepository.findByFilterWithOrdering(categoryId, startDate, endDate, location, productDelivery, keywords, productStatus, orderDirection, orderBy);

        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);

        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("location", location);
        model.addAttribute("delivery", delivery);
        model.addAttribute("keywords", keywords);
        model.addAttribute("status", status);
        model.addAttribute("orderDirection", orderDirection);
        model.addAttribute("orderBy", orderBy);
        
        return "products";
    }


    @GetMapping("/products/{id}")
    public String  getProductDetailsPage(Model model, @PathVariable Long id) {
        Optional<Item> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Item product = productOptional.get();
            model.addAttribute("product", product);
            return "product";
        }
        return "redirect:/products";
    }
    
    
}
