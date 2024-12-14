package com.example.DonationManager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Column;

import java.sql.Date;
import java.time.LocalDate;

import com.example.DonationManager.model.enumeration.*;


@Entity
public class FilterSaver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;

    private String location;
    private String keyWords;
    private LocalDate dateFin;
    private LocalDate createdDate;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = true)
    private ProductDelivery delivery;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = true)
    private ProductStatus status;

 
    @ManyToOne
    @JoinColumn(name = "category_id")  
    private Category category;


    @ManyToOne
    @JoinColumn(name = "user_id")  
    private AppUser user;


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keywords) {
        this.keyWords = keywords;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getCreatedAt() {
        return createdDate;
    }

    public void setCreatedAt(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate date) {
        this.dateFin = date;
    }


    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public ProductDelivery getDelivery() {
        return delivery;
    }

    public void setDelivery(ProductDelivery delivery) {
        this.delivery = delivery;
    }


}
