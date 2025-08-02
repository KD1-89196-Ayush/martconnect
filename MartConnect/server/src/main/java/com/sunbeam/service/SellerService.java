package com.sunbeam.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.sunbeam.entities.Seller;

/**
 * Service interface for Seller entity operations
 */
public interface SellerService extends BaseService<Seller, Integer> {
    
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
    List<Seller> findByFirstName(String firstName);
    
    /**
     * Find sellers by last name
     */
    List<Seller> findByLastName(String lastName);
    
    /**
     * Find sellers by shop name
     */
    List<Seller> findByShopName(String shopName);
    
    /**
     * Find sellers by first name, last name, or shop name containing search term
     */
    List<Seller> findByFirstNameOrLastNameOrShopNameContaining(String searchTerm);
    
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
     * Find sellers by city
     */
    List<Seller> findByCity(String city);
    
    /**
     * Find sellers by state
     */
    List<Seller> findByState(String state);
    
    /**
     * Count total sellers
     */
    long countTotalSellers();
    
    /**
     * Find sellers created in a specific date range
     */
    List<Seller> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Register a new seller
     */
    Seller registerSeller(Seller seller);
    
    /**
     * Update seller profile
     */
    Seller updateSellerProfile(Integer sellerId, Seller seller);
    
    /**
     * Change seller password
     */
    boolean changePassword(Integer sellerId, String oldPassword, String newPassword);
    
    /**
     * Authenticate seller
     */
    Optional<Seller> authenticate(String email, String password);
    
    /**
     * Get seller statistics
     */
    Object[] getSellerStatistics(Integer sellerId);
    
    /**
     * Get top sellers by product count
     */
    List<Object[]> getTopSellersByProductCount(int limit);
    
    /**
     * Get top sellers by order count
     */
    List<Object[]> getTopSellersByOrderCount(int limit);
    
    /**
     * Get top sellers by revenue
     */
    List<Object[]> getTopSellersByRevenue(int limit);
    
    /**
     * Get sellers with low stock products
     */
    List<Seller> getSellersWithLowStockProducts(Integer lowStockThreshold);
    
    /**
     * Get sellers with out of stock products
     */
    List<Seller> getSellersWithOutOfStockProducts();
} 