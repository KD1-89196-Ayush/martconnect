package com.sunbeam.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"category", "seller", "orderItems", "cartItems", "productImages"})
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;
    
    @NotBlank(message = "Product name is required")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @NotBlank(message = "Unit is required")
    @Column(name = "unit", nullable = false, length = 20)
    private String unit;
    
    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    @Column(name = "stock", nullable = false)
    private Integer stock;
    
    @Column(name = "image_url", length = 255)
    private String imageUrl;
    
    @NotNull(message = "Category is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;
    
    // Getter methods to expose category and seller info in JSON responses
    @com.fasterxml.jackson.annotation.JsonProperty("category_id")
    public Integer getCategoryId() {
        return category != null ? category.getCategoryId() : null;
    }
    
    @com.fasterxml.jackson.annotation.JsonProperty("category")
    public String getCategoryName() {
        return category != null ? category.getName() : null;
    }
    
    @NotNull(message = "Seller is required")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seller_id", nullable = false)
    @JsonIgnore
    private Seller seller;
    
    @com.fasterxml.jackson.annotation.JsonProperty("seller_id")
    public Integer getSellerId() {
        return seller != null ? seller.getSellerId() : null;
    }
    
    @com.fasterxml.jackson.annotation.JsonProperty("seller")
    public String getSellerName() {
        return seller != null ? seller.getShopName() : null;
    }
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = jakarta.persistence.CascadeType.ALL)
    @JsonIgnore
    private List<OrderItem> orderItems = new ArrayList<>();
    
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = jakarta.persistence.CascadeType.ALL)
    @JsonIgnore
    private List<Cart> cartItems = new ArrayList<>();
    
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = jakarta.persistence.CascadeType.ALL)
    @JsonIgnore
    private List<ProductImage> productImages = new ArrayList<>();
} 