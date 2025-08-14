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
    
} 