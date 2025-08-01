package com.sunbeam.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sunbeam.entities.Category;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {
    
    /**
     * Find category by name
     */
    Optional<Category> findByName(String name);
    
    /**
     * Find categories by name containing search term
     */
    List<Category> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find categories by description containing search term
     */
    List<Category> findByDescriptionContainingIgnoreCase(String description);
    
    /**
     * Find categories by name or description containing search term
     */
    @Query("SELECT c FROM Category c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Category> findByNameOrDescriptionContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Check if category exists by name
     */
    boolean existsByName(String name);
    
    /**
     * Count categories by name
     */
    long countByNameContainingIgnoreCase(String name);
    
    /**
     * Find categories created in a specific date range
     */
    @Query("SELECT c FROM Category c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<Category> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                         @Param("endDate") java.time.LocalDateTime endDate);
    
    /**
     * Find categories with product count
     */
    @Query("SELECT c, COUNT(p) as productCount FROM Category c LEFT JOIN c.products p GROUP BY c ORDER BY productCount DESC")
    List<Object[]> findCategoriesWithProductCount();
    
    /**
     * Find categories with product count greater than specified value
     */
    @Query("SELECT c, COUNT(p) as productCount FROM Category c LEFT JOIN c.products p GROUP BY c HAVING COUNT(p) > :minProductCount ORDER BY productCount DESC")
    List<Object[]> findCategoriesWithProductCountGreaterThan(@Param("minProductCount") long minProductCount);
    
    /**
     * Find categories with product count less than or equal to specified value
     */
    @Query("SELECT c, COUNT(p) as productCount FROM Category c LEFT JOIN c.products p GROUP BY c HAVING COUNT(p) <= :maxProductCount ORDER BY productCount ASC")
    List<Object[]> findCategoriesWithProductCountLessThanEqual(@Param("maxProductCount") long maxProductCount);
    
    /**
     * Find categories with average product price
     */
    @Query("SELECT c, AVG(p.price) as avgPrice FROM Category c LEFT JOIN c.products p GROUP BY c ORDER BY avgPrice DESC")
    List<Object[]> findCategoriesWithAveragePrice();
    
    /**
     * Find categories with total product stock
     */
    @Query("SELECT c, SUM(p.stock) as totalStock FROM Category c LEFT JOIN c.products p GROUP BY c ORDER BY totalStock DESC")
    List<Object[]> findCategoriesWithTotalStock();
    
    /**
     * Find categories with low stock products (stock less than or equal to specified value)
     */
    @Query("SELECT c, COUNT(p) as lowStockCount FROM Category c LEFT JOIN c.products p ON p.stock <= :lowStockThreshold GROUP BY c HAVING COUNT(p) > 0 ORDER BY lowStockCount DESC")
    List<Object[]> findCategoriesWithLowStockProducts(@Param("lowStockThreshold") Integer lowStockThreshold);
    
    /**
     * Find categories with out of stock products (stock = 0)
     */
    @Query("SELECT c, COUNT(p) as outOfStockCount FROM Category c LEFT JOIN c.products p ON p.stock = 0 GROUP BY c HAVING COUNT(p) > 0 ORDER BY outOfStockCount DESC")
    List<Object[]> findCategoriesWithOutOfStockProducts();
    
    /**
     * Find categories with products by price range
     */
    @Query("SELECT c, COUNT(p) as productCount FROM Category c LEFT JOIN c.products p ON p.price BETWEEN :minPrice AND :maxPrice GROUP BY c HAVING COUNT(p) > 0 ORDER BY productCount DESC")
    List<Object[]> findCategoriesWithProductsByPriceRange(@Param("minPrice") java.math.BigDecimal minPrice, 
                                                         @Param("maxPrice") java.math.BigDecimal maxPrice);
    
    /**
     * Find categories with products by seller
     */
    @Query("SELECT c, COUNT(p) as productCount FROM Category c LEFT JOIN c.products p ON p.seller.sellerId = :sellerId GROUP BY c HAVING COUNT(p) > 0 ORDER BY productCount DESC")
    List<Object[]> findCategoriesWithProductsBySeller(@Param("sellerId") Integer sellerId);
    
    /**
     * Find categories with products by seller email
     */
    @Query("SELECT c, COUNT(p) as productCount FROM Category c LEFT JOIN c.products p ON p.seller.email = :sellerEmail GROUP BY c HAVING COUNT(p) > 0 ORDER BY productCount DESC")
    List<Object[]> findCategoriesWithProductsBySellerEmail(@Param("sellerEmail") String sellerEmail);
    
    /**
     * Find categories with products by seller name (first name, last name, or shop name)
     */
    @Query("SELECT c, COUNT(p) as productCount FROM Category c LEFT JOIN c.products p ON LOWER(p.seller.firstName) LIKE LOWER(CONCAT('%', :sellerName, '%')) OR LOWER(p.seller.lastName) LIKE LOWER(CONCAT('%', :sellerName, '%')) OR LOWER(p.seller.shopName) LIKE LOWER(CONCAT('%', :sellerName, '%')) GROUP BY c HAVING COUNT(p) > 0 ORDER BY productCount DESC")
    List<Object[]> findCategoriesWithProductsBySellerName(@Param("sellerName") String sellerName);
    
    /**
     * Find categories with products by unit
     */
    @Query("SELECT c, COUNT(p) as productCount FROM Category c LEFT JOIN c.products p ON LOWER(p.unit) LIKE LOWER(CONCAT('%', :unit, '%')) GROUP BY c HAVING COUNT(p) > 0 ORDER BY productCount DESC")
    List<Object[]> findCategoriesWithProductsByUnit(@Param("unit") String unit);
    
    /**
     * Find categories with products created in a specific date range
     */
    @Query("SELECT c, COUNT(p) as productCount FROM Category c LEFT JOIN c.products p ON p.createdAt BETWEEN :startDate AND :endDate GROUP BY c HAVING COUNT(p) > 0 ORDER BY productCount DESC")
    List<Object[]> findCategoriesWithProductsByCreatedDate(@Param("startDate") java.time.LocalDateTime startDate, 
                                                          @Param("endDate") java.time.LocalDateTime endDate);
    
    /**
     * Find categories with products updated in a specific date range
     */
    @Query("SELECT c, COUNT(p) as productCount FROM Category c LEFT JOIN c.products p ON p.updatedAt BETWEEN :startDate AND :endDate GROUP BY c HAVING COUNT(p) > 0 ORDER BY productCount DESC")
    List<Object[]> findCategoriesWithProductsByUpdatedDate(@Param("startDate") java.time.LocalDateTime startDate, 
                                                          @Param("endDate") java.time.LocalDateTime endDate);
    
    /**
     * Find categories with order count
     */
    @Query("SELECT c, COUNT(oi) as orderCount FROM Category c LEFT JOIN c.products p LEFT JOIN p.orderItems oi GROUP BY c ORDER BY orderCount DESC")
    List<Object[]> findCategoriesWithOrderCount();
    
    /**
     * Find categories with revenue
     */
    @Query("SELECT c, SUM(oi.quantity * oi.pricePerUnit) as totalRevenue FROM Category c LEFT JOIN c.products p LEFT JOIN p.orderItems oi GROUP BY c ORDER BY totalRevenue DESC")
    List<Object[]> findCategoriesWithRevenue();
    
    /**
     * Find categories with cart count
     */
    @Query("SELECT c, COUNT(cart) as cartCount FROM Category c LEFT JOIN c.products p LEFT JOIN p.cartItems cart GROUP BY c ORDER BY cartCount DESC")
    List<Object[]> findCategoriesWithCartCount();
    
    /**
     * Find categories with cart value
     */
    @Query("SELECT c, SUM(cart.quantity * p.price) as totalCartValue FROM Category c LEFT JOIN c.products p LEFT JOIN p.cartItems cart GROUP BY c ORDER BY totalCartValue DESC")
    List<Object[]> findCategoriesWithCartValue();
} 