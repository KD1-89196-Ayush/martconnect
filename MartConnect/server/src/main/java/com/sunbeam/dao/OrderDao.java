package com.sunbeam.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sunbeam.entities.Order;
import com.sunbeam.entities.Order.PaymentStatus;

public interface OrderDao extends JpaRepository<Order, Long> {
    
    //find orders by customer
    List<Order> findByCustomerId(Long customerId);
    
    //find orders by seller
    List<Order> findBySellerId(Long sellerId);
    
    //find orders by payment status
    List<Order> findByPaymentStatus(PaymentStatus paymentStatus);
    
    //find orders by customer and payment status
    List<Order> findByCustomerIdAndPaymentStatus(Long customerId, PaymentStatus paymentStatus);
    
    //find orders by seller and payment status
    List<Order> findBySellerIdAndPaymentStatus(Long sellerId, PaymentStatus paymentStatus);
    
    //find orders by total amount range
    List<Order> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    
    //find orders by transaction id
    List<Order> findByTransactionIdContaining(String transactionId);
    
    //count by customer
    long countByCustomerId(Long customerId);
    
    //count by seller
    long countBySellerId(Long sellerId);
    
    //count by payment status
    long countByPaymentStatus(PaymentStatus paymentStatus);
} 