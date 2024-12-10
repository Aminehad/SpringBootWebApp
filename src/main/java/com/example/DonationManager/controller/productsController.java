package com.example.DonationManager.controller;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.example.DonationManager.model.Category;
import com.example.DonationManager.model.Item;
import com.example.DonationManager.model.enumeration.ProductDelivery;
import com.example.DonationManager.model.enumeration.ProductStatus;
import com.example.DonationManager.repository.CategoryRepository;
import com.example.DonationManager.repository.ProductRepository;
import com.example.DonationManager.repository.UserRepository;
import com.example.services.ProductService;

import org.springframework.ui.Model;


@Controller
public class productsController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;


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
            Long userId = (long) 1;
            Item product = productOptional.get();
            boolean favorite = product.getFavoritedByUsers().contains(userRepository.findById(userId).get());
            model.addAttribute("product", product);
            model.addAttribute("favorite", favorite);
            return "product";
        }
        return "redirect:/products";
    }
    
    @PostMapping("/products")
    public String addProduct(
        @RequestParam("picture") MultipartFile picture,
        @RequestParam("title") String title,
        @RequestParam("description") String description,
        @RequestParam("categoryId") Long categoryId,
        @RequestParam("status") String status,
        @RequestParam("location") String location,
        @RequestParam("keywords") String keywords,
        @RequestParam("delivery") String delivery,
        @RequestParam("userId") Long userId,
        Model model) {

    try {
        // Créer un nouvel objet Item
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(description);
        if (categoryRepository.findById(categoryId).isPresent()) {
            item.setCategory(categoryRepository.findById(categoryId).get()); 
        }
        if (userRepository.findById(userId).isPresent()) {
            item.setUser(userRepository.findById(userId).get()); 
        }
        item.setStatus(ProductStatus.valueOf(status));
        item.setLocation(location);
        item.setKeyWords(keywords);
        item.setDelivery(ProductDelivery.valueOf(delivery));
        Date currentSqlDate = new Date(System.currentTimeMillis());
        item.setCreatedAt(currentSqlDate);

        if (!picture.isEmpty()) {
            String uploadsDir = "src/main/resources/static/uploads";
            String realPath = new File(uploadsDir).getAbsolutePath(); 
            String fileName = System.currentTimeMillis() + "_" + picture.getOriginalFilename();

            File uploadDir = new File(realPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            File destinationFile = new File(realPath + File.separator + fileName);
            picture.transferTo(destinationFile); 

            item.setPicture("/uploads/" + fileName);
        }

 
        productRepository.save(item);


        model.addAttribute("successMessage", "Produit ajouté avec succès !");
        } catch (Exception e) {
            System.out.println( e.getMessage());
            // Ajouter un message d'erreur
            model.addAttribute("errorMessage", "Erreur lors de l'ajout du produit : " + e.getMessage());
        }

    return "redirect:/user/" + userId; 
}

    @GetMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        Integer userId = 1;
        productRepository.deleteById(id);
        return "redirect:/user/" + userId;  
    }
    
}
