package com.sunbeam.service;

import java.util.List;

import com.sunbeam.dto.CartDto;

/**
 * Service interface for Cart entity operations
 */
public interface CartService extends BaseService<CartDto, Integer> {
    
    /**
     * Add item to cart
     */
    CartDto addToCart(CartDto cartDto);
    
    /**
     * Update cart item quantity
     */
    CartDto updateQuantity(Integer cartId, Integer newQuantity);
    
    /**
     * Remove item from cart
     */
    void removeFromCart(Integer cartId);
    
    /**
     * Find cart items by customer
     */
    List<CartDto> findByCustomer(Integer customerId);
    
    /**
     * Get cart item count for customer
     */

} 