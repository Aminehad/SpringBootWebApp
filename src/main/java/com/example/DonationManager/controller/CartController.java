package com.example.DonationManager.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.DonationManager.model.AppUser;
import com.example.DonationManager.model.Cart;
import com.example.DonationManager.model.Item;
import com.example.DonationManager.repository.UserRepository;
import com.example.services.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/cart")
    public String getCart(HttpSession session, Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() &&
            !(authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)) {
             String email = authentication.getName();
            model.addAttribute("email", email);
            Optional<AppUser> userDetailsOptional = userRepository.findByEmail(email);
            AppUser userDetails = userDetailsOptional.orElse(null);
            if (userDetails != null ) {
                model.addAttribute("userId", userDetails.getId());
                model.addAttribute("userName", userDetails.getName());
            }else {
                model.addAttribute("userId", null);
                model.addAttribute("userName", "Guest");
            }
        } else {
            model.addAttribute("userName", "Guest");
        }

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        model.addAttribute("cart", cart);
        return "cart"; 
    }
    private final ProductService productService;

    public CartController(ProductService productService) {
        this.productService = productService;
    }
    
    @PostMapping("/cart/add")
    public String addItemToCart(HttpSession session, @RequestParam("productId") Long productId, RedirectAttributes redirectAttributes) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
    
        Item product = productService.findById(productId); 
        if (product == null) {
            throw new IllegalArgumentException("Invalid product ID");
        }

        boolean isProductInCart = false;
        for (Item item : cart.getItems()) {
            if (item.getId().equals(product.getId())) {
                isProductInCart = true;
                break;
            }
        }
    
        if (!isProductInCart) {
            cart.addItem(product);
            redirectAttributes.addFlashAttribute("message", "Item added to cart!");
        } else {
            redirectAttributes.addFlashAttribute("message", "This item is already in your cart!");
        }
        
        return "redirect:/cart"; 
    }

    @GetMapping("/cart/{id}/remove")
    public String deleteItemToCart(HttpSession session, @PathVariable Long id,RedirectAttributes redirectAttributes) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        Item product = null;
        for (Item item : cart.getItems()) {
            if (item.getId().equals(id)) {
                product = item;
                break;
            }
        }
    
        if (product != null) {
            cart.removeItem(product);
            redirectAttributes.addFlashAttribute("message", "Element supprime avec succes");
        }
        
        return "redirect:/cart"; 
    }
}
