package com.example.DonationManager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.example.DonationManager.model.AppUser;
import com.example.services.UserService;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginPage(
            @RequestParam(name = "loginError", required = false) String loginError,
            @RequestParam(name = "messageSuccess", required = false) String messageSuccess,
            Model model) {

        if (loginError != null) {
            model.addAttribute("messageErr", "Email ou mot de passe incorrect");
            model.addAttribute("message", null);
        } else if (messageSuccess != null) {
            model.addAttribute("message", messageSuccess);
            model.addAttribute("messageErr", null);
        } else {
            model.addAttribute("messageErr", null);
            model.addAttribute("message", null);
        }
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String processRegistration(@ModelAttribute AppUser user, Model model,
            RedirectAttributes redirectAttributes) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("messageError", "User with this email already exists!");
            return "register";
        }
        AppUser savedUser = userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message", "vous pouvez authentifier avec votre coordonnées");
        return "redirect:/login";
    }

}
