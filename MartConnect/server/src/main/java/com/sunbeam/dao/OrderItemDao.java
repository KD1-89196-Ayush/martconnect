package com.sunbeam.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sunbeam.entities.OrderItem;

public interface OrderItemDao extends JpaRepository<OrderItem, Long> {
    
    //find order items by order
    List<OrderItem> findByOrderId(Long orderId);
    
    //find order items by product
    List<OrderItem> findByProductId(Long productId);
    
    //find order items by quantity range
    List<OrderItem> findByQuantityBetween(Integer minQuantity, Integer maxQuantity);
    
    //find order items by price per unit range
    List<OrderItem> findByPricePerUnitBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    //find order items by order and product
    List<OrderItem> findByOrderIdAndProductId(Long orderId, Long productId);
    
    //count by order
    long countByOrderId(Long orderId);
    
    //count by product
    long countByProductId(Long productId);
} 