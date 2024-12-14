package com.example.DonationManager.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.DonationManager.model.AppUser;
import com.example.DonationManager.model.Category;
import com.example.DonationManager.model.Item;
import com.example.DonationManager.repository.CategoryRepository;
import com.example.DonationManager.repository.ProductRepository;
import com.example.DonationManager.repository.UserRepository;


@Controller
public class UserController {
    

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;


    @GetMapping("/user/{userId}/favorite/{itemId}")
    public String addFavorite(@PathVariable Long userId, @PathVariable Long itemId) {

        System.out.println("we ar here \n");
        AppUser user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        Item item = productRepository.findById(itemId)
            .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé"));

        if (user.getFavoriteItems().contains(item)) {
            user.getFavoriteItems().remove(item);
            userRepository.save(user);
            System.out.println("nooooo");
            System.out.println(user.getFavoriteItems());
        }else {
            user.getFavoriteItems().add(item);
            userRepository.save(user);
            System.out.println("yees");
            System.out.println(user.getFavoriteItems());
        }

        return "redirect:/products/" + itemId; 
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/user/{id}")
    public String getUserPage(@PathVariable Long id,@RequestParam(name = "category", required = false) Long category, Model model) {

        Optional<AppUser> userOp =  userRepository.findById(id);

        if (userOp.isPresent()) {


            AppUser user = userOp.get();
            List<Item> products;
            if (category != null && category == 1) {
                products = user.getFavoriteItems().stream().toList();
            }
            else if ( category != null && category == 2) {
                products = productRepository.findByUserId(id);
            }
            else  {
                products = productRepository.findByUserId(id);
            }
            List<Category> categories = categoryRepository.findAll();

            model.addAttribute("products", products);
            model.addAttribute("user", user);
            model.addAttribute("category", category);
            model.addAttribute("categories", categories);

    
            return "userProducts";
        }
        return "redirect:/products";

    }
    

}
