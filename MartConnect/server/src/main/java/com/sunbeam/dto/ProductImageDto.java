package com.sunbeam.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageDto {
    
    private Integer imageId;
    
    @NotNull(message = "Product ID is required")
    private Integer productId;
    
    @NotBlank(message = "Image URL is required")
    private String imageUrl;
    
    private String altText;
    
    private Boolean isPrimary = false;
    
    private Integer sortOrder = 0;
    
    private LocalDateTime createdAt;
} 