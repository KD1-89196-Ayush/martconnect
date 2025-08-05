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
    
    /**
     * Find customers by first name
     */
    List<CustomerDto> findByFirstName(String firstName);
    
    /**
     * Find customers by last name
     */
    List<CustomerDto> findByLastName(String lastName);
    
    /**
     * Find customers by phone
     */
    List<CustomerDto> findByPhone(String phone);
    
    /**
     * Find customers by address containing
     */
    List<CustomerDto> findByAddressContaining(String address);
    
    /**
     * Count customers by first name
     */
    long countByFirstName(String firstName);
    
    /**
     * Count customers by last name
     */
    long countByLastName(String lastName);
    
    /**
     * Count customers by phone
     */
    long countByPhone(String phone);
} 