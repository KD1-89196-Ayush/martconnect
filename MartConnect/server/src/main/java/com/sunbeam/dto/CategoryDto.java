package com.sunbeam.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    
    private Integer categoryId;
    
    @NotBlank(message = "Category name is required")
    private String name;
    
    private String description;
    
    private LocalDateTime createdAt;
} 