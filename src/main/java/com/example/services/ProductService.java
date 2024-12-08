package com.example.services;

import org.springframework.stereotype.Service;

import com.example.DonationManager.model.enumeration.ProductDelivery;
import com.example.DonationManager.model.enumeration.ProductStatus;

@Service
public class ProductService {

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