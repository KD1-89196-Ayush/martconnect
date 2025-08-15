package com.sunbeam.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
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
} 