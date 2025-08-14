package com.sunbeam.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sunbeam.dto.ProductDto;

/**
 * Service interface for Product entity operations
 */
public interface ProductService extends BaseService<ProductDto, Integer> {
    
    /**
     * Add product
     */
    ProductDto addProduct(ProductDto productDto);
    
    /**
     * Update product
     */
    ProductDto updateProduct(Integer productId, ProductDto productDto);
    
    /**
     * Add product with category management
     */
    ProductDto addProductWithCategory(ProductDto productDto);
    
    /**
     * Update product with category management
     */
    ProductDto updateProductWithCategory(Integer productId, ProductDto productDto);
    
    /**
     * Update product stock
     */
    ProductDto updateStock(Integer productId, Integer newStock);
    
    /**
     * Update product price
     */
    ProductDto updatePrice(Integer productId, BigDecimal newPrice);
    
    /**
     * Find products by name
     */
    List<ProductDto> findByName(String name);
    
    /**
     * Find products by category
     */
    List<ProductDto> findByCategory(String categoryName);
    
    /**
     * Find products by seller
     */
    List<ProductDto> findBySeller(Integer sellerId);
    
    
    /**
     * Find products by name or description containing
     */
    List<ProductDto> findByNameOrDescriptionContaining(String searchTerm);
    
    /**
     * Search products by name and category
     */
    List<ProductDto> searchProductsByNameAndCategory(String name, String category);
    
    /**
     * Search products with multiple criteria
     */
    List<ProductDto> searchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice, Integer minStock, String unit);
    
    /**
     * Get products with low stock by seller
     */
    List<ProductDto> getProductsWithLowStockBySeller(Integer sellerId, Integer lowStockThreshold);
    
    /**
     * Get out of stock products by seller
     */
    List<ProductDto> getOutOfStockProductsBySeller(Integer sellerId);
} 