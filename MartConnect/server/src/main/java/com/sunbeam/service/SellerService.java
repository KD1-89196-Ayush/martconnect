package com.sunbeam.service;

import java.util.List;
import java.util.Optional;

import com.sunbeam.dto.SellerDto;

/**
 * Service interface for Seller entity operations
 */
public interface SellerService extends BaseService<SellerDto, Integer> {
    
    /**
     * Register new seller
     */
    SellerDto registerSeller(SellerDto sellerDto);
    
    /**
     * Authenticate seller
     */
    SellerDto authenticate(String email, String password);
    
    /**
     * Find seller by email
     */
    Optional<SellerDto> findByEmail(String email);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Find sellers by first name
     */
    List<SellerDto> findByFirstName(String firstName);
    
    /**
     * Find sellers by last name
     */
    List<SellerDto> findByLastName(String lastName);
    
    /**
     * Find sellers by phone
     */
    List<SellerDto> findByPhone(String phone);
    
    /**
     * Find sellers by shop name
     */
    List<SellerDto> findByShopName(String shopName);
    
    /**
     * Find sellers by shop address containing
     */
    List<SellerDto> findByShopAddressContaining(String shopAddress);
    
    /**
     * Count sellers by first name
     */
    long countByFirstName(String firstName);
    
    /**
     * Count sellers by last name
     */
    long countByLastName(String lastName);
    
    /**
     * Count sellers by phone
     */
    long countByPhone(String phone);
    
    /**
     * Count sellers by shop name
     */
    long countByShopName(String shopName);
} 