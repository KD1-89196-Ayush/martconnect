package com.sunbeam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sunbeam.entities.Cart;

@Repository
public interface CartDao extends JpaRepository<Cart, Integer> {
    
    /**
     * Find cart items by customer
     */
    List<Cart> findByCustomer_CustomerId(Integer customerId);
    
    /**
     * Find cart items by customer and product
     */
    List<Cart> findByCustomer_CustomerIdAndProduct_ProductId(Integer customerId, Integer productId);
    
    /**
     * Count cart items by customer
     */
    long countByCustomer_CustomerId(Integer customerId);
} 