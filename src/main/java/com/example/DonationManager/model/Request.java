package com.example.DonationManager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.time.LocalDateTime;
@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private AppUser owner; // The user who owns the product

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private AppUser requester; // The user who requested the item

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item; // The item being requested

    private String status; // E.g., "PENDING", "CONFIRMED", "REJECTED"

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Long getId () {
        return  id;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return this.status;
    }
    public void setRequester (AppUser requester) {
        this.requester = requester;
    }
    public AppUser getRequester() {
        return this.requester;
    }
    public void setOwner (AppUser owner) {
        this.owner = owner;
    }
    public AppUser getOwner() {
        return this.owner;
    }
    public void setItem (Item item) {
        this.item = item;
    }

    public Item getItem () {
        return  item;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}