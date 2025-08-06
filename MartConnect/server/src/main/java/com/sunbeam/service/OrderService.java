package com.sunbeam.service;

import java.math.BigDecimal;
import java.util.List;

import com.sunbeam.dto.ApiResponse;
import com.sunbeam.dto.OrderDto;
import com.sunbeam.entities.Order;
import com.sunbeam.entities.Order.PaymentStatus;

public interface OrderService extends BaseService<Order, Long> {
    
    // GET methods - return DTOs for frontend
    OrderDto getOrderById(Long orderId);
    List<OrderDto> getAllOrders();
    List<OrderDto> getCustomerOrders(Long customerId);
    List<OrderDto> getSellerOrders(Long sellerId);
    List<OrderDto> getOrdersByPaymentStatus(PaymentStatus paymentStatus);
    List<OrderDto> getOrdersByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    List<OrderDto> getOrdersByTransactionIdContaining(String transactionId);
    
    // POST/PUT/DELETE methods - return ApiResponse
    ApiResponse createOrder(OrderDto orderDto);
    ApiResponse updateOrder(Long orderId, OrderDto orderDto);
    ApiResponse deleteOrder(Long orderId);
    
    // Business logic methods
    long countByCustomer(Long customerId);
    long countBySeller(Long sellerId);
    long countByPaymentStatus(PaymentStatus paymentStatus);
} 