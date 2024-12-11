package com.example.DonationManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.example.DonationManager", "com.example.services"})
public class DonationManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonationManagerApplication.class, args);
	}

}
