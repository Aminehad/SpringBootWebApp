package com.example.DonationManager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.DonationManager.model.AppUser;
import com.example.DonationManager.model.Item;
import com.example.DonationManager.model.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByRequesterAndItemAndStatus(AppUser requester, Item item, String status);
}

