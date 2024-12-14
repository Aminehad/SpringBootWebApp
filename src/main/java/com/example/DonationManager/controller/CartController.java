package com.example.DonationManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.DonationManager.model.Cart;
import com.example.DonationManager.model.Item;
import com.example.services.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
    @GetMapping("/cart")
    public String getCart(HttpSession session, Model model) {
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
}
