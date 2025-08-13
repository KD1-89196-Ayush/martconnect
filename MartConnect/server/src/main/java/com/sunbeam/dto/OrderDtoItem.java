package com.sunbeam.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    
    private Integer orderItemId;
    
    @NotNull(message = "Order ID is required")
    private Integer orderId;
    
    @NotNull(message = "Product ID is required")
    private Integer productId;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    @NotNull(message = "Price per unit is required")
    @DecimalMin(value = "0.01", message = "Price per unit must be greater than 0")
    private BigDecimal pricePerUnit;
    
    private LocalDateTime createdAt;
    
    // Additional fields for display
    private String productName;
    private String productImageUrl;
    private BigDecimal totalPrice; // quantity * pricePerUnit
} 