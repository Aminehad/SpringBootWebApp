package com.example.DonationManager.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

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
import com.example.DonationManager.model.Request;
import com.example.DonationManager.repository.ProductRepository;
import com.example.DonationManager.repository.RequestRepository;
import com.example.DonationManager.repository.UserRepository;
import com.example.services.ProductService;
import com.example.services.RequestService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {


    private final ProductService productService;
    private final RequestService requestService;

    private final UserRepository userRepository;

    private final RequestRepository reqRepository;

    private final ProductRepository productRepository;

    public CartController(ProductService productService, RequestService requestService, UserRepository userRepository,RequestRepository requestRepository, ProductRepository productRepository) {
        this.productService = productService;
        this.requestService = requestService;
        this.userRepository = userRepository;
        this.reqRepository = requestRepository;
        this.productRepository = productRepository;
    }

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
            redirectAttributes.addFlashAttribute("message", "Vous avez ajouté un produit :)");
        } else {
            redirectAttributes.addFlashAttribute("message", "ce produit est deja existe :)");
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
                redirectAttributes.addFlashAttribute("message", "Vous avez déjà une demande en attente pour l'article: " + item.getTitle());
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

        redirectAttributes.addFlashAttribute("message", "Vous aves valider votre demance avec succes e, attendant la confiramtion de propietaire :)");
        session.removeAttribute("cart"); 
        return "redirect:/cart"; // Redirect back to the cart page
    }

    @GetMapping("/cart/accepteRequest/{id}")
    public String accepteCartRequests(HttpSession session,RedirectAttributes redirectAttributes, @PathVariable Long id) {
        Request request = reqRepository.findById(id).get();
                    request.setStatus("CONFIRMED");
                    reqRepository.save(request);
                    Item item = request.getItem();
                    item.setDisabled(true);
                    productRepository.save(item);
                    redirectAttributes.addFlashAttribute("message", "Vous aves valider votre demance avec succes e, attendant la confiramtion de propietaire :)");
        return "redirect:/user/1?category=3";
    }

    @GetMapping("/cart/refuserRequest/{id}")
    public String refuserCartRequests(HttpSession session,RedirectAttributes redirectAttributes, @PathVariable Long id) {
        Request request = reqRepository.findById(id).get();
                    request.setStatus("REJECTED");
                    reqRepository.save(request);

        return "redirect:/user/1?category=3";
    }

}
