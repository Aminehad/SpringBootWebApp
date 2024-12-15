package com.example.DonationManager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.DonationManager.model.AppUser;
import com.example.DonationManager.model.Item;
import com.example.DonationManager.model.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    boolean isPr = false;

    Optional<Request> findByRequesterAndItemAndStatus(AppUser requester, Item item, String status);
    
    List<Request> findByRequester(AppUser requester);
    
    List<Request> findByOwner(AppUser owner);

    List<Request> findByOwnerAndStatusIsNullOrStatus(AppUser owner, String status);

    @Query("SELECT r.item FROM Request r WHERE r IN :requests")
    List<Item> findItemsByRequests(List<Request> requests);
}

