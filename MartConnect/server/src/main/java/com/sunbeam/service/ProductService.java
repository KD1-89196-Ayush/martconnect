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
     * Find products by price range
     */
    List<ProductDto> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Find products by stock greater than
     */
    List<ProductDto> findByStockGreaterThan(Integer minStock);
    
    /**
     * Find products by stock less than or equal
     */
    List<ProductDto> findByStockLessThanEqual(Integer maxStock);
    
    /**
     * Find products by unit
     */
    List<ProductDto> findByUnit(String unit);
    
    /**
     * Find products by name or description containing
     */
    List<ProductDto> findByNameOrDescriptionContaining(String searchTerm);
    
    /**
     * Find products by categories
     */
    List<ProductDto> findByCategories(List<String> categories);
    
    /**
     * Find products by seller and category
     */
    List<ProductDto> findBySellerAndCategory(Integer sellerId, String categoryName);
    
    /**
     * Find products by seller and price range
     */
    List<ProductDto> findBySellerAndPriceRange(Integer sellerId, BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Find all products with pagination
     */
    Page<ProductDto> findAll(Pageable pageable);
    
    /**
     * Find products by category with pagination
     */
    Page<ProductDto> findByCategory(String categoryName, Pageable pageable);
    
    /**
     * Find products by seller with pagination
     */
    Page<ProductDto> findBySeller(Integer sellerId, Pageable pageable);
    
    /**
     * Find products by name or description containing with pagination
     */
    Page<ProductDto> findByNameOrDescriptionContaining(String searchTerm, Pageable pageable);
    
    /**
     * Count products by seller
     */
    long countBySeller(Integer sellerId);
    
    /**
     * Count products by category
     */
    long countByCategory(String categoryName);
    
    /**
     * Find products with low stock
     */
    List<ProductDto> findProductsWithLowStock(Integer lowStockThreshold);
    
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