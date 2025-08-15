package com.sunbeam.service;

import java.util.List;
import java.util.Optional;

import com.sunbeam.dto.CustomerDto;

/**
 * Service interface for Customer entity operations
 */
public interface CustomerService extends BaseService<CustomerDto, Integer> {
    
    /**
     * Register new customer
     */
    CustomerDto registerCustomer(CustomerDto customerDto);
    
    /**
     * Authenticate customer
     */
    CustomerDto authenticate(String email, String password);
    
    /**
     * Find customer by email
     */
    Optional<CustomerDto> findByEmail(String email);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
    
} 