package com.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DonationManager.model.enumeration.ProductDelivery;
import com.example.DonationManager.model.enumeration.ProductStatus;
import com.example.DonationManager.repository.ProductRepository;
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
}