package com.example.DonationManager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.DonationManager.model.AppUser;
import com.example.DonationManager.model.Item;
import com.example.DonationManager.model.ItemNotification;



@Repository
public interface ItemNotificationRepository extends JpaRepository<ItemNotification, Long> {

    List<ItemNotification> findByUserIdAndIsNewTrue(Long userId);

    List<ItemNotification> findByUserId(Long userId);

    @Query("SELECT n.item FROM ItemNotification n WHERE n.user.id = :userId")
    List<Item> findAllItemsByUser(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE ItemNotification in SET in.isNew = false WHERE in.user.id = :userId")
    void updateAllIsNewFalseByUserId(@Param("userId") Long userId);
}