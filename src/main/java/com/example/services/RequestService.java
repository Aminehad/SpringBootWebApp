package com.example.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.DonationManager.model.Item;
import com.example.DonationManager.model.AppUser;
import com.example.DonationManager.model.Request;
import com.example.DonationManager.repository.RequestRepository;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository; // Repository to interact with the database

    public void save(Request request) {
        requestRepository.save(request);
    }
    public Optional <Request> findByRequesterAndItemAndStatus(AppUser requester, Item item, String status) {
        return requestRepository.findByRequesterAndItemAndStatus(requester, item, status);
    }

}
