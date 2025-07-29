package com.sunbeam.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "areas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Area {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "area_id")
    private Integer areaId;
    
    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be 6 digits")
    @NotBlank(message = "Pincode is required")
    @Column(name = "pincode", nullable = false, length = 10)
    private String pincode;
    
    @NotBlank(message = "Area name is required")
    @Column(name = "area_name", nullable = false, length = 100)
    private String areaName;
    
    @NotBlank(message = "City is required")
    @Column(name = "city", nullable = false, length = 50)
    private String city;
    
    @NotBlank(message = "State is required")
    @Column(name = "state", nullable = false, length = 50)
    private String state;
    
    @NotNull(message = "Customer is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
} 