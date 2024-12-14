package com.example.DonationManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.DonationManager.model.FilterSaver;

@Repository
public interface FilterRepository extends JpaRepository<FilterSaver, Long> {

}
