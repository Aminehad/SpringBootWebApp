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
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.DonationManager.model.enumeration.*;



@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;

    private String title;
    private String description;
    private String location;
    private String keyWords;
    private String picture;


    @Enumerated(EnumType.STRING) 
    @Column(nullable = false)
    private ProductStatus status;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = false)
    private ProductDelivery delivery;
 

    @ManyToOne
    @JoinColumn(name = "category_id")  
    private Category category;

    private String condition;

    @Column(name = "created_at")
    @DateTimeFormat(pattern = "dd-MM-yyy")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")  
    private AppUser user;

    @ManyToMany(mappedBy = "favoriteItems")
    private Set<AppUser> favoritedByUsers = new HashSet<>();

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


    public Set<AppUser> getFavoritedByUsers() {
        return favoritedByUsers;
    }

    public void setFavoritedByUsers(Set<AppUser> favoritedByUsers) {
        this.favoritedByUsers = favoritedByUsers;
    }
}
