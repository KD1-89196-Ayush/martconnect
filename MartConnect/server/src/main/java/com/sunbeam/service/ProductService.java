package com.sunbeam.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sunbeam.entities.Product;

/**
 * Service interface for Product entity operations
 */
public interface ProductService extends BaseService<Product, Integer> {
    
    /**
     * Find products by name
     */
    List<Product> findByName(String name);
    
    /**
     * Find products by category
     */
    List<Product> findByCategory(String categoryName);
    
    /**
     * Find products by seller
     */
    List<Product> findBySeller(Integer sellerId);
    
    /**
     * Find products by seller email
     */
    List<Product> findBySellerEmail(String sellerEmail);
    
    /**
     * Find products by price range
     */
    List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Find products with stock greater than specified value
     */
    List<Product> findByStockGreaterThan(Integer minStock);
    
    /**
     * Find products with stock less than or equal to specified value
     */
    List<Product> findByStockLessThanEqual(Integer maxStock);
    
    /**
     * Find products by unit
     */
    List<Product> findByUnit(String unit);
    
    /**
     * Find products by name or description containing search term
     */
    List<Product> findByNameOrDescriptionContaining(String searchTerm);
    
    /**
     * Find products by multiple categories
     */
    List<Product> findByCategories(List<String> categories);
    
    /**
     * Find products by seller and category
     */
    List<Product> findBySellerAndCategory(Integer sellerId, String categoryName);
    
    /**
     * Find products by seller and price range
     */
    List<Product> findBySellerAndPriceRange(Integer sellerId, BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Find products with pagination
     */
    Page<Product> findAll(Pageable pageable);
    
    /**
     * Find products by category with pagination
     */
    Page<Product> findByCategory(String categoryName, Pageable pageable);
    
    /**
     * Find products by seller with pagination
     */
    Page<Product> findBySeller(Integer sellerId, Pageable pageable);
    
    /**
     * Find products by name or description with pagination
     */
    Page<Product> findByNameOrDescriptionContaining(String searchTerm, Pageable pageable);
    
    /**
     * Count products by seller
     */
    long countBySeller(Integer sellerId);
    
    /**
     * Count products by category
     */
    long countByCategory(String categoryName);
    
    /**
     * Find products created in a specific date range
     */
    List<Product> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find products updated in a specific date range
     */
    List<Product> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find products with low stock
     */
    List<Product> findProductsWithLowStock(Integer lowStockThreshold);
    
    /**
     * Add product
     */
    Product addProduct(Product product);
    
    /**
     * Update product
     */
    Product updateProduct(Integer productId, Product product);
    
    /**
     * Update product stock
     */
    Product updateStock(Integer productId, Integer newStock);
    
    /**
     * Update product price
     */
    Product updatePrice(Integer productId, BigDecimal newPrice);
    
    /**
     * Search products with filters
     */
    List<Product> searchProducts(String searchTerm, String category, BigDecimal minPrice, 
                                BigDecimal maxPrice, Integer minStock, String unit);
    
    /**
     * Get products with low stock by seller
     */
    List<Product> getProductsWithLowStockBySeller(Integer sellerId, Integer lowStockThreshold);
    
    /**
     * Get out of stock products by seller
     */
    List<Product> getOutOfStockProductsBySeller(Integer sellerId);

    /**
     * Add product with category management
     */
    Product addProductWithCategory(Product product);
    
    /**
     * Update product with category management
     */
    Product updateProductWithCategory(Integer productId, Product product);
} 