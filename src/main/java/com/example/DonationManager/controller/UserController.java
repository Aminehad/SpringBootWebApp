package com.example.DonationManager.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.DonationManager.model.AppUser;
import com.example.DonationManager.model.Category;
import com.example.DonationManager.model.FilterSaver;
import com.example.DonationManager.model.Item;
import com.example.DonationManager.model.Request;
import com.example.DonationManager.model.enumeration.ProductDelivery;
import com.example.DonationManager.model.enumeration.ProductStatus;
import com.example.DonationManager.repository.CategoryRepository;
import com.example.DonationManager.repository.ProductRepository;
import com.example.DonationManager.repository.RequestRepository;
import com.example.DonationManager.repository.UserRepository;
import com.example.services.ProductService;

@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    RequestRepository requestRepository;

    @GetMapping("/user/{userId}/favorite/{itemId}")
    public String addFavorite(@PathVariable Long userId, @PathVariable Long itemId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUser user;
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
            String email = authentication.getName();
            Optional<AppUser> userDetailsOptional = userRepository.findByEmail(email);
            user = userDetailsOptional.orElse(null);
            if (user == null) {
                return "redirect:/login";
            }
        } else {
            return "redirect:/login";
        }

        Item item = productRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé"));

        if (user.getFavoriteItems().contains(item)) {
            user.getFavoriteItems().remove(item);
            userRepository.save(user);
        } else {
            user.getFavoriteItems().add(item);
            userRepository.save(user);
            System.out.println(user.getFavoriteItems());
        }

        return "redirect:/products/" + itemId;
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/user/{id}")
    public String getUserPage(@PathVariable Long id, @RequestParam(name = "category", required = false) Long category,
            Model model) {

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

        List<Item> products = null;
        List<Request> requests = null;
        if (category != null && category == 1) {
            products = user.getFavoriteItems().stream().toList();
        } else if (category != null && category == 2) {
            requests = requestRepository.findByRequester(user);
        } else if (category != null && category == 3) {
            requests = requestRepository.findByOwner(user);

        } else {
            products = productRepository.findByUserId(id);
        }

        Integer productsNum = productRepository.findByUserId(id).size();
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("size", productsNum);
        model.addAttribute("products", products);
        model.addAttribute("requests", requests);
        
        model.addAttribute("user", user);
        model.addAttribute("category", category);
        model.addAttribute("categories", categories);

        return "userProducts";

    }

}
