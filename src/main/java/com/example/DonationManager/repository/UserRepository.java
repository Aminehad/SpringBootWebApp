package com.example.DonationManager.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.DonationManager.model.AppUser;


@Repository
public interface UserRepository extends JpaRepository<AppUser, Long> {
}
