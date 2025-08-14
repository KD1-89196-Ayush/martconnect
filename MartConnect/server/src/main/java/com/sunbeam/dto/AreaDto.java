package com.sunbeam.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaDto {
    
    private Integer areaId;
    
    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be 6 digits")
    @NotBlank(message = "Pincode is required")
    private String pincode;
    
    @NotBlank(message = "Area name is required")
    private String areaName;
    
    @NotBlank(message = "City is required")
    private String city;
    
    @NotBlank(message = "State is required")
    private String state;
    
    @NotNull(message = "Customer ID is required")
    private Integer customerId;
    
    private LocalDateTime createdAt;
} 