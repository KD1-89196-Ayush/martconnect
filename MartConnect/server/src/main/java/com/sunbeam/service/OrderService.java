package com.sunbeam.service;

import java.util.List;

import com.sunbeam.dto.OrderDto;

/**
 * Service interface for Order entity operations
 */
public interface OrderService extends BaseService<OrderDto, Integer> {
    
    /**
     * Create order with validation and stock update
     */
    OrderDto createOrder(OrderDto orderDto);
    
    /**
     * Cancel order and restore stock
     */
//    OrderDto cancelOrder(Integer orderId);

    /**
     * Find orders by customer with seller and items
     */
    List<OrderDto> findByCustomerWithSellerAndItems(Integer customerId);
    
    /**
     * Find orders by seller with customer and items
     */
    List<OrderDto> findBySellerWithCustomerAndItems(Integer sellerId);
} 