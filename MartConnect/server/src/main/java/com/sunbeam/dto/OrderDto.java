package com.sunbeam.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.sunbeam.entities.Order.PaymentStatus;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    
    private Integer orderId;
    
    @NotNull(message = "Customer ID is required")
    private Integer customerId;
    
    @NotNull(message = "Seller ID is required")
    private Integer sellerId;
    
    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;
    
    @DecimalMin(value = "0.00", message = "Delivery charge cannot be negative")
    private BigDecimal deliveryCharge = BigDecimal.ZERO;
    
    private PaymentStatus paymentStatus = PaymentStatus.PAID;
    
    private LocalDateTime orderDate;
    
    private String transactionId;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Additional fields for display
    private String customerName;
    private String sellerName;
    private String shopName;
    
    // Customer object for frontend compatibility
    private CustomerDto customer;
    
    // Seller object for frontend compatibility
    private SellerDto seller;
    
    private List<OrderItemDto> orderItems;
} 