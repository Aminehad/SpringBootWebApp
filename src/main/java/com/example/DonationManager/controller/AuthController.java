package com.example.DonationManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import com.example.DonationManager.model.AppUser;
import com.example.services.UserService;


@Controller
public class AuthController {
    
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute AppUser user, Model model) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("message", "User with this email already exists!");
            return "register";
        }
        AppUser savedUser = userService.saveUser(user);
            model.addAttribute("message", "User " + savedUser.getName() + " registered successfully!");
            return "redirect:/login";  
       
    }

 
}
