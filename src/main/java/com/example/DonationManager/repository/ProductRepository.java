package com.example.DonationManager.repository;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.DonationManager.model.Item;
import com.example.DonationManager.model.enumeration.ProductDelivery;
import com.example.DonationManager.model.enumeration.ProductStatus;


@Repository
public interface ProductRepository extends JpaRepository<Item, Long> {


@Query("SELECT i FROM Item i WHERE " +
       "(:category IS NULL OR i.category.id = :category) AND " +
       "(:startDate IS NULL OR i.createdAt >= :startDate) AND " +
       "(:endDate IS NULL OR i.createdAt <= :endDate) AND " +
       "(:location IS NULL OR LOWER(i.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
       "(:delivery IS NULL  OR i.delivery = :delivery) AND " +
       "(:keywords IS NULL OR LOWER(i.keyWords) LIKE LOWER(CONCAT('%', :keywords, '%'))) AND " +
       "(:status IS NULL OR  i.status = :status)")        // Fallback to sorting by ID)
List<Item> findByFilter(@Param("category") Long categoryId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("location") String location,
                        @Param("delivery") ProductDelivery delivery,
                        @Param("keywords") String keywords,
                        @Param("status") ProductStatus status ,
                        Sort sort);

default List<Item> findByFilterWithOrdering(Long categoryId, LocalDate startDate, LocalDate endDate, String location,
                        ProductDelivery delivery, String keywords, ProductStatus status, String orderDirection, Integer orderBy) {
       Sort sort;
       Integer orderByInSwitch = orderBy == null ? 0 : orderBy;
       switch (orderByInSwitch) {
              case 1:
                  if ("DESC".equalsIgnoreCase(orderDirection)) {
                     sort = Sort.by(Sort.Direction.DESC, "createdAt");
                  } else {
                     sort = Sort.by(Sort.Direction.ASC, "createdAt");
                  }
                  break;
              case 2:
                  if ("DESC".equalsIgnoreCase(orderDirection)) {
                     sort = Sort.by(Sort.Direction.DESC, "location");
                  } else {
                     sort = Sort.by(Sort.Direction.ASC, "location");
                  }
                  break;
              case 3:
                  if ("DESC".equalsIgnoreCase(orderDirection)) {
                     sort = Sort.by(Sort.Direction.DESC, "status");
                  } else {
                     sort = Sort.by(Sort.Direction.ASC, "status");
                  }
                  break;        
              default:
                  if ("DESC".equalsIgnoreCase(orderDirection)) {
                     sort = Sort.by(Sort.Direction.DESC, "id");
                  } else {
                     sort = Sort.by(Sort.Direction.ASC, "id");
                  }
                  break;
          }


       return findByFilter(categoryId, startDate, endDate, location, delivery, keywords, status,sort);
       }


   @Query("SELECT i FROM Item i WHERE i.user.id = :userId")
   List<Item> findByUserId(@Param("userId") Long userId);
}
