package com.example.DonationManager.controller;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.DonationManager.model.Category;
import com.example.DonationManager.model.FilterSaver;
import com.example.DonationManager.model.Item;
import com.example.DonationManager.model.enumeration.ProductDelivery;
import com.example.DonationManager.model.enumeration.ProductStatus;
import com.example.DonationManager.repository.CategoryRepository;
import com.example.DonationManager.repository.FilterRepository;
import com.example.DonationManager.repository.ProductRepository;
import com.example.DonationManager.repository.UserRepository;
import com.example.services.EmailService;
import com.example.services.ProductService;

import jakarta.mail.MessagingException;

import com.example.DonationManager.model.AppUser;

import org.springframework.ui.Model;

@Controller
public class productsController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FilterRepository filterRepository;

    @Autowired
    EmailService emailService;


    @GetMapping({"/products","/"})
    public String getProductsPage(
            @RequestParam(name = "category", required = false) Long categoryId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(name = "location", required = false) String location,
            @RequestParam(name = "delivery", required = false) String delivery,
            @RequestParam(name = "keywords", required = false) String keywords,
            @RequestParam(name = "isFilter", required = false) String isFilter,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "orderDirection", required = false) String orderDirection,
            @RequestParam(name = "orderBy", required = false) Integer orderBy,
            Model model, Principal principal) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            model.addAttribute("email", email);
            Optional<AppUser> userDetailsOptional = userRepository.findByEmail(email);
            AppUser userDetails = userDetailsOptional.orElse(null);

            if (userDetails != null) {
                model.addAttribute("userId", userDetails.getId());
                model.addAttribute("userName", userDetails.getName());
                if (isFilter != null && isFilter.equals("true")) {
                    FilterSaver item = new FilterSaver();
                    if (categoryId != null && categoryRepository.findById(categoryId).isPresent()) {
                        item.setCategory(categoryRepository.findById(categoryId).get());
                    }
                    if (ProductService.isValidProductStatus(status) ) item.setStatus(ProductStatus.valueOf(status));
        
                    item.setUser(userDetails);
                    if (location != null) item.setLocation(location);
                    if (keywords != null) item.setKeyWords(keywords);
                    if (ProductService.isValidProductDelivery(delivery)) item.setDelivery(ProductDelivery.valueOf(delivery));
                    if (startDate != null) item.setCreatedAt(startDate);
                    if (endDate != null) item.setDateFin(endDate);
                    filterRepository.save(item);
                }
            } else {
                model.addAttribute("userId", null);
                model.addAttribute("userName", "Guest");
            }

        } else {
            model.addAttribute("userId", null);
            model.addAttribute("userName", "Guest");
        }

        ProductDelivery productDelivery = null;
        ProductStatus productStatus = null;

        if (ProductService.isValidProductDelivery(delivery)) {
            productDelivery = ProductDelivery.valueOf(delivery.toUpperCase());
        }
        if (ProductService.isValidProductStatus(status)) {
            productStatus = ProductStatus.valueOf(status.toUpperCase());
        }

        List<Item> products = productRepository.findByFilterWithOrdering(categoryId, startDate, endDate, location,
                productDelivery, keywords, productStatus, orderDirection, orderBy);

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
    public String getProductDetailsPage(Model model, @PathVariable Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user;
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Optional<AppUser> userDetailsOptional = userRepository.findByEmail(email);
            user = userDetailsOptional.orElse(null);
        } else {
            user =  null;
        }

        Optional<Item> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Item product = productOptional.get();
            model.addAttribute("product", product);
            if (user != null) {
                model.addAttribute("userName", user.getName());
                model.addAttribute("userId", user.getId());
                boolean favorite = product.getFavoritedByUsers().contains(user);
                model.addAttribute("favorite", favorite);
            }

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
            item.setDisabled(false);
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

            List<FilterSaver> filters = filterRepository.findAll();

        // Vérification des filtres pour envoyer des notifications
        System.out.println("filter");
        for (FilterSaver filter : filters) {
            System.out.println(filter.getId());
            if (ProductService.matchesFilter(item, filter)) {
                System.out.println("match" );
                // emailService.sendSimpleMessage(
                //     filter.getUser().getEmail(),
                //     "Nouvel item correspondant à votre filtre",
                //     "Un nouvel item correspondant à vos critères a été ajouté : " + item.getTitle()
                // );
            }
        }

            model.addAttribute("successMessage", "Produit ajouté avec succès !");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            // Ajouter un message d'erreur
            model.addAttribute("errorMessage", "Erreur lors de l'ajout du produit : " + e.getMessage());
        }

        return "redirect:/user/" + userId;
    }

    @GetMapping("/products/{id}/delete")
    public String deleteProduct(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user;
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Optional<AppUser> userDetailsOptional = userRepository.findByEmail(email);
            user = userDetailsOptional.orElse(null);
            if (user == null || user.getId() != id) {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }
        productRepository.deleteById(user.getId());
        return "redirect:/user/" + user.getId();
    }

}
