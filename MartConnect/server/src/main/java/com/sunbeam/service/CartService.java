package com.sunbeam.service;

import java.math.BigDecimal;
import java.util.List;


import com.sunbeam.entities.Cart;

/**
 * Service interface for Cart entity operations
 */
public interface CartService extends BaseService<Cart, Integer> {
    
    /**
     * Find cart items by customer
     */
    List<Cart> findByCustomer(Integer customerId);
    
    /**
     * Find cart items by product
     */
    List<Cart> findByProduct(Integer productId);
    
    /**
     * Find cart items by seller
     */
    List<Cart> findBySeller(Integer sellerId);
    
    /**
     * Find cart items by customer and product
     */
    List<Cart> findByCustomerAndProduct(Integer customerId, Integer productId);
    
    /**
     * Count cart items by customer
     */
    long countByCustomer(Integer customerId);
    
    /**
     * Count cart items by product
     */
    long countByProduct(Integer productId);
    
    /**
     * Count cart items by seller
     */
    long countBySeller(Integer sellerId);
    
    /**
     * Find cart items with low stock
     */
    List<Cart> findCartItemsWithLowStock();
    
    /**
     * Find cart items by customer with low stock
     */
    List<Cart> findCartItemsByCustomerWithLowStock(Integer customerId);
    
    /**
     * Add item to cart
     */
    Cart addToCart(Integer customerId, Integer productId, Integer quantity);
    
    /**
     * Update cart item quantity
     */
    Cart updateCartItemQuantity(Integer cartId, Integer newQuantity);
    
    /**
     * Remove item from cart
     */
    void removeFromCart(Integer cartId);
    
    /**
     * Clear customer cart
     */
    void clearCustomerCart(Integer customerId);
    
    /**
     * Get customer cart total
     */
    BigDecimal getCustomerCartTotal(Integer customerId);
    
    /**
     * Get customer cart item count
     */
    long getCustomerCartItemCount(Integer customerId);
} 