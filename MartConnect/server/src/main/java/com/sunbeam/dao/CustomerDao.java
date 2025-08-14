package com.sunbeam.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sunbeam.entities.Customer;

@Repository
public interface CustomerDao extends JpaRepository<Customer, Integer> {
    
    /**
     * Find customer by email
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Check if customer exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if customer exists by phone
     */
    boolean existsByPhone(String phone);
} 