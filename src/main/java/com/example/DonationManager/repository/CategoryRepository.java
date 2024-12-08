
package com.example.DonationManager.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.DonationManager.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
