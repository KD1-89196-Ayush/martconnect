package com.sunbeam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
     * Find cart items by product
     */
    List<Cart> findByProduct_ProductId(Integer productId);
    
    /**
     * Find cart items by seller
     */
    @Query("SELECT c FROM Cart c WHERE c.product.seller.sellerId = :sellerId")
    List<Cart> findBySellerId(@Param("sellerId") Integer sellerId);
    
    /**
     * Find cart items by customer and product
     */
    List<Cart> findByCustomer_CustomerIdAndProduct_ProductId(Integer customerId, Integer productId);
    
    /**
     * Count cart items by customer
     */
    long countByCustomer_CustomerId(Integer customerId);
    
    /**
     * Count cart items by product
     */
    long countByProduct_ProductId(Integer productId);
    
    /**
     * Count cart items by seller
     */
    @Query("SELECT COUNT(c) FROM Cart c WHERE c.product.seller.sellerId = :sellerId")
    long countBySellerId(@Param("sellerId") Integer sellerId);
    
    /**
     * Find cart items with low stock (quantity greater than product stock)
     */
    @Query("SELECT c FROM Cart c WHERE c.quantity > c.product.stock")
    List<Cart> findCartItemsWithLowStock();
    
    /**
     * Find cart items by customer with low stock
     */
    @Query("SELECT c FROM Cart c WHERE c.customer.customerId = :customerId AND c.quantity > c.product.stock")
    List<Cart> findCartItemsByCustomerWithLowStock(@Param("customerId") Integer customerId);
} 