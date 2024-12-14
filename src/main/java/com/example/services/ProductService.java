package com.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DonationManager.model.enumeration.ProductDelivery;
import com.example.DonationManager.model.enumeration.ProductStatus;
import com.example.DonationManager.repository.ProductRepository;
import com.example.DonationManager.model.FilterSaver;
import com.example.DonationManager.model.Item;
@Service
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;
    
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    public Item findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public static boolean isValidProductStatus(String status) {
        if (status == null) {
            return false;
        }
        try {
            ProductStatus.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean isValidProductDelivery(String delivery) {
        if (delivery == null) {
            return false;
        }
        try {
            ProductDelivery.valueOf(delivery);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static boolean matchesFilter(Item item, FilterSaver filter) {
        
        if (filter.getLocation() != null && !matchesLike(item.getLocation(), filter.getLocation())) {
            System.out.println("dalse 2");
            return false;
        }
        if (filter.getKeyWords() != null && !item.getKeyWords().contains(filter.getKeyWords())) {
            System.out.println("false 3");
            return false;
        }
        if (filter.getCategory() != null && !item.getCategory().equals(filter.getCategory())) {
            System.out.println("false 4");
            return false;
        }
        if (filter.getDelivery() != null && !item.getDelivery().equals(filter.getDelivery())) {
            System.out.println("false 5");
            return false;
        }
        if (filter.getStatus() != null && !item.getStatus().equals(filter.getStatus())) {
            System.out.println("false 6");
            return false;
        }

        return true;
    }

    private static boolean matchesLike(String value, String pattern) {
        if (value == null || pattern == null) {
            return false;
        }
    

        String regex = pattern
            .replace("%", ".*")  
            .replace("_", "."); 
        
        // Vérifier la correspondance
        return value.toLowerCase().matches(regex.toLowerCase());
    }


    
}