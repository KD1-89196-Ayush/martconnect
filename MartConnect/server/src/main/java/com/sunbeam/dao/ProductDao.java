package com.sunbeam.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sunbeam.entities.Product;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
    
    /**
     * Find products by name
     */
    List<Product> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find products by category
     */
    List<Product> findByCategory_NameContainingIgnoreCase(String categoryName);
    
    /**
     * Find products by seller
     */
    List<Product> findBySeller_SellerId(Integer sellerId);
    
    /**
     * Find products by seller email
     */
    List<Product> findBySeller_Email(String sellerEmail);
    
    /**
     * Find products by price range
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
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
    List<Product> findByUnitContainingIgnoreCase(String unit);
    
    /**
     * Find products by name or description containing search term
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Product> findByNameOrDescriptionContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Find products by multiple categories
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.category) IN :categories")
    List<Product> findByCategories(@Param("categories") List<String> categories);
    
    /**
     * Find products by seller and category
     */
    List<Product> findBySeller_SellerIdAndCategory_NameContainingIgnoreCase(Integer sellerId, String categoryName);
    
    /**
     * Find products by seller and price range
     */
    @Query("SELECT p FROM Product p WHERE p.seller.sellerId = :sellerId AND p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findBySellerAndPriceRange(@Param("sellerId") Integer sellerId, 
                                           @Param("minPrice") BigDecimal minPrice, 
                                           @Param("maxPrice") BigDecimal maxPrice);
    
    /**
     * Find products with pagination
     */
    @Override
    Page<Product> findAll(Pageable pageable);
    
    /**
     * Find products by category with pagination
     */
    Page<Product> findByCategory_NameContainingIgnoreCase(String categoryName, Pageable pageable);
    
    /**
     * Find products by seller with pagination
     */
    Page<Product> findBySeller_SellerId(Integer sellerId, Pageable pageable);
    
    /**
     * Find products by name or description with pagination
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(p.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Product> findByNameOrDescriptionContaining(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    /**
     * Count products by seller
     */
    long countBySeller_SellerId(Integer sellerId);
    
    /**
     * Count products by category
     */
    long countByCategory_NameContainingIgnoreCase(String categoryName);
    
    /**
     * Find products created in a specific date range
     */
    @Query("SELECT p FROM Product p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Product> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                        @Param("endDate") java.time.LocalDateTime endDate);
    
    /**
     * Find products updated in a specific date range
     */
    @Query("SELECT p FROM Product p WHERE p.updatedAt BETWEEN :startDate AND :endDate")
    List<Product> findByUpdatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                        @Param("endDate") java.time.LocalDateTime endDate);
    
    /**
     * Find products with low stock (less than or equal to specified value)
     */
    @Query("SELECT p FROM Product p WHERE p.stock <= :lowStockThreshold ORDER BY p.stock ASC")
    List<Product> findProductsWithLowStock(@Param("lowStockThreshold") Integer lowStockThreshold);
} 