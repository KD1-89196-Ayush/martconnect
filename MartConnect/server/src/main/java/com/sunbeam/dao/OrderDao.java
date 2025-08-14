package com.sunbeam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sunbeam.entities.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
    
    /**
     * Find orders by seller with customer and order items
     */
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.customer LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product WHERE o.seller.sellerId = :sellerId")
    List<Order> findBySellerWithCustomerAndItems(@Param("sellerId") Integer sellerId);
    
    /**
     * Find orders by customer with seller and order items
     */
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.seller LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.product WHERE o.customer.customerId = :customerId")
    List<Order> findByCustomerWithSellerAndItems(@Param("customerId") Integer customerId);
} 