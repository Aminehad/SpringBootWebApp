package com.example.DonationManager.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.DonationManager.model.AppUser;
import com.example.DonationManager.model.Cart;
import com.example.DonationManager.model.Item;
import com.example.DonationManager.model.Request;
import com.example.DonationManager.repository.UserRepository;
import com.example.services.ProductService;
import com.example.services.RequestService;

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
    private final RequestService requestService;
    private final UserRepository userRepository;

    public CartController(ProductService productService, RequestService requestService, UserRepository userRepository) {
        this.productService = productService;
        this.requestService = requestService;
        this.userRepository = userRepository;
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

    @PostMapping("/cart/confirmRequest")
    public String confirmAllRequests(HttpSession session,RedirectAttributes redirectAttributes) {
        // Retrieve the cart from the session
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<AppUser> userDetailsOptional = userRepository.findByEmail(userName);
        AppUser userDetails = userDetailsOptional.orElse(null);
        AppUser loggedInUser = userDetails;
        

        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.getItems().isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Your cart is empty.");
            return "redirect:/cart"; 
        }

          // Check if the user already has pending requests for any of the items in the cart
        for (Item item : cart.getItems()) {
            Optional<Request> existingRequest = requestService.findByRequesterAndItemAndStatus(loggedInUser, item, "PENDING");
            if (existingRequest.isPresent()) {
                // If there is an existing pending request for this item, clear the item from his cart and show an error message
                redirectAttributes.addFlashAttribute("message", "You already have a pending request for item: " + item.getTitle());
                cart.removeItem(item);
                return "redirect:/cart";
            }
        }
    
        for (Item item : cart.getItems()) {
            AppUser itemOwner = item.getUser(); 
            if (itemOwner != null) {
               // Save the order to track the request
                Request  request = new Request();
                request.setOwner(itemOwner); 
                request.setRequester(loggedInUser);
                request.setItem(item); 
                request.setStatus("PENDING"); // Initial status is PENDING
                request.setCreatedAt(LocalDateTime.now());
                request.setUpdatedAt(LocalDateTime.now());
                
                requestService.save(request);
            }
        }

        redirectAttributes.addFlashAttribute("message", "Confirmation requests sent to item owners.");
        session.removeAttribute("cart"); 
        return "redirect:/cart"; // Redirect back to the cart page
    }

}
