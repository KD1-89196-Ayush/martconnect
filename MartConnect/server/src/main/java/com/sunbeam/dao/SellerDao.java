package com.sunbeam.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sunbeam.entities.Seller;

@Repository
public interface SellerDao extends JpaRepository<Seller, Integer> {
    
    /**
     * Find seller by email
     */
    Optional<Seller> findByEmail(String email);
    
    /**
     * Find seller by phone number
     */
    Optional<Seller> findByPhone(String phone);
    
    /**
     * Find sellers by first name
     */
    List<Seller> findByFirstNameContainingIgnoreCase(String firstName);
    
    /**
     * Find sellers by last name
     */
    List<Seller> findByLastNameContainingIgnoreCase(String lastName);
    
    /**
     * Find sellers by shop name
     */
    List<Seller> findByShopNameContainingIgnoreCase(String shopName);
    
    /**
     * Find sellers by first name, last name, or shop name containing the search term
     */
    @Query("SELECT s FROM Seller s WHERE LOWER(s.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(s.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(s.shopName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Seller> findByFirstNameOrLastNameOrShopNameContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Check if seller exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if seller exists by phone
     */
    boolean existsByPhone(String phone);
    
    /**
     * Check if seller exists by shop name
     */
    boolean existsByShopName(String shopName);
    
    /**
     * Find sellers by city (from shop address)
     */
    @Query("SELECT s FROM Seller s WHERE LOWER(s.shopAddress) LIKE LOWER(CONCAT('%', :city, '%'))")
    List<Seller> findByCity(@Param("city") String city);
    
    /**
     * Find sellers by state (from shop address)
     */
    @Query("SELECT s FROM Seller s WHERE LOWER(s.shopAddress) LIKE LOWER(CONCAT('%', :state, '%'))")
    List<Seller> findByState(@Param("state") String state);
    
    /**
     * Count total sellers
     */
    @Query("SELECT COUNT(s) FROM Seller s")
    long countTotalSellers();
    
    /**
     * Find sellers created in a specific date range
     */
    @Query("SELECT s FROM Seller s WHERE s.createdAt BETWEEN :startDate AND :endDate")
    List<Seller> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                       @Param("endDate") java.time.LocalDateTime endDate);
    
    /**
     * Find sellers with products count
     */
    @Query("SELECT s, COUNT(p) as productCount FROM Seller s LEFT JOIN s.products p GROUP BY s ORDER BY productCount DESC")
    List<Object[]> findSellersWithProductCount();
    
    /**
     * Find top sellers by order count
     */
    @Query("SELECT s, COUNT(o) as orderCount FROM Seller s LEFT JOIN s.orders o GROUP BY s ORDER BY orderCount DESC")
    List<Object[]> findTopSellersByOrderCount();
} 