package com.example.DonationManager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class UserController {
    

    @GetMapping("/user/{id}/products")
    public String getMethodName(@PathVariable Integer id) {
        return "userProducts";
    }
    
}
