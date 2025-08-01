package com.sunbeam.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sunbeam.entities.ProductImage;

@Repository
public interface ProductImageDao extends JpaRepository<ProductImage, Integer> {
    
    /**
     * Find product images by product
     */
    List<ProductImage> findByProduct_ProductId(Integer productId);
    
    /**
     * Find product images by product and primary status
     */
    List<ProductImage> findByProduct_ProductIdAndIsPrimary(Integer productId, Boolean isPrimary);
    
    /**
     * Find primary product image by product
     */
    Optional<ProductImage> findByProduct_ProductIdAndIsPrimaryTrue(Integer productId);
    
    /**
     * Find product images by image URL
     */
    List<ProductImage> findByImageUrlContaining(String imageUrl);
    
    /**
     * Find product images by alt text
     */
    List<ProductImage> findByAltTextContainingIgnoreCase(String altText);
    
    /**
     * Find product images by alt text or image URL containing search term
     */
    @Query("SELECT pi FROM ProductImage pi WHERE LOWER(pi.altText) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(pi.imageUrl) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<ProductImage> findByAltTextOrImageUrlContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Find product images by sort order range
     */
    List<ProductImage> findBySortOrderBetween(Integer minSortOrder, Integer maxSortOrder);
    
    /**
     * Find product images by sort order greater than specified value
     */
    List<ProductImage> findBySortOrderGreaterThan(Integer minSortOrder);
    
    /**
     * Find product images by sort order less than or equal to specified value
     */
    List<ProductImage> findBySortOrderLessThanEqual(Integer maxSortOrder);
    
    /**
     * Find product images by product and sort order range
     */
    List<ProductImage> findByProduct_ProductIdAndSortOrderBetween(Integer productId, Integer minSortOrder, Integer maxSortOrder);
    
    /**
     * Find product images by primary status
     */
    List<ProductImage> findByIsPrimary(Boolean isPrimary);
    
    /**
     * Find product images by primary status and product
     */
    List<ProductImage> findByIsPrimaryAndProduct_ProductId(Boolean isPrimary, Integer productId);
    
    /**
     * Count product images by product
     */
    long countByProduct_ProductId(Integer productId);
    
    /**
     * Count product images by product and primary status
     */
    long countByProduct_ProductIdAndIsPrimary(Integer productId, Boolean isPrimary);
    
    /**
     * Count product images by primary status
     */
    long countByIsPrimary(Boolean isPrimary);
    
    /**
     * Find product images created in a specific date range
     */
    @Query("SELECT pi FROM ProductImage pi WHERE pi.createdAt BETWEEN :startDate AND :endDate")
    List<ProductImage> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                             @Param("endDate") java.time.LocalDateTime endDate);
    
    /**
     * Find product images by seller
     */
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.seller.sellerId = :sellerId")
    List<ProductImage> findBySellerId(@Param("sellerId") Integer sellerId);
    
    /**
     * Find product images by seller email
     */
    @Query("SELECT pi FROM ProductImage pi WHERE pi.product.seller.email = :sellerEmail")
    List<ProductImage> findBySellerEmail(@Param("sellerEmail") String sellerEmail);
    
    /**
     * Find product images by seller name (first name, last name, or shop name)
     */
    @Query("SELECT pi FROM ProductImage pi WHERE LOWER(pi.product.seller.firstName) LIKE LOWER(CONCAT('%', :sellerName, '%')) OR LOWER(pi.product.seller.lastName) LIKE LOWER(CONCAT('%', :sellerName, '%')) OR LOWER(pi.product.seller.shopName) LIKE LOWER(CONCAT('%', :sellerName, '%'))")
    List<ProductImage> findBySellerName(@Param("sellerName") String sellerName);
    
    /**
     * Find product images by product name
     */
    @Query("SELECT pi FROM ProductImage pi WHERE LOWER(pi.product.name) LIKE LOWER(CONCAT('%', :productName, '%'))")
    List<ProductImage> findByProductName(@Param("productName") String productName);
    
    /**
     * Find product images by product category
     */
    @Query("SELECT pi FROM ProductImage pi WHERE LOWER(pi.product.category) LIKE LOWER(CONCAT('%', :category, '%'))")
    List<ProductImage> findByProductCategory(@Param("category") String category);
    
    /**
     * Find product images by product and primary status with product details
     */
    @Query("SELECT pi, pi.product FROM ProductImage pi WHERE pi.product.productId = :productId AND pi.isPrimary = :isPrimary")
    List<Object[]> findProductImagesByProductAndPrimaryWithProduct(@Param("productId") Integer productId, 
                                                                  @Param("isPrimary") Boolean isPrimary);
    
    /**
     * Find product images by product with product details
     */
    @Query("SELECT pi, pi.product FROM ProductImage pi WHERE pi.product.productId = :productId ORDER BY pi.sortOrder ASC")
    List<Object[]> findProductImagesByProductWithProduct(@Param("productId") Integer productId);
    
    /**
     * Find product images by seller with product details
     */
    @Query("SELECT pi, pi.product FROM ProductImage pi WHERE pi.product.seller.sellerId = :sellerId ORDER BY pi.product.name ASC, pi.sortOrder ASC")
    List<Object[]> findProductImagesBySellerWithProduct(@Param("sellerId") Integer sellerId);
    
    /**
     * Find product images by seller email with product details
     */
    @Query("SELECT pi, pi.product FROM ProductImage pi WHERE pi.product.seller.email = :sellerEmail ORDER BY pi.product.name ASC, pi.sortOrder ASC")
    List<Object[]> findProductImagesBySellerEmailWithProduct(@Param("sellerEmail") String sellerEmail);
    
    /**
     * Find product images by seller name with product details
     */
    @Query("SELECT pi, pi.product FROM ProductImage pi WHERE LOWER(pi.product.seller.firstName) LIKE LOWER(CONCAT('%', :sellerName, '%')) OR LOWER(pi.product.seller.lastName) LIKE LOWER(CONCAT('%', :sellerName, '%')) OR LOWER(pi.product.seller.shopName) LIKE LOWER(CONCAT('%', :sellerName, '%')) ORDER BY pi.product.name ASC, pi.sortOrder ASC")
    List<Object[]> findProductImagesBySellerNameWithProduct(@Param("sellerName") String sellerName);
    
    /**
     * Find product images by product name with product details
     */
    @Query("SELECT pi, pi.product FROM ProductImage pi WHERE LOWER(pi.product.name) LIKE LOWER(CONCAT('%', :productName, '%')) ORDER BY pi.product.name ASC, pi.sortOrder ASC")
    List<Object[]> findProductImagesByProductNameWithProduct(@Param("productName") String productName);
    
    /**
     * Find product images by product category with product details
     */
    @Query("SELECT pi, pi.product FROM ProductImage pi WHERE LOWER(pi.product.category) LIKE LOWER(CONCAT('%', :category, '%')) ORDER BY pi.product.name ASC, pi.sortOrder ASC")
    List<Object[]> findProductImagesByProductCategoryWithProduct(@Param("category") String category);
    
    /**
     * Find product images with product count by seller
     */
    @Query("SELECT pi.product.seller, COUNT(pi) as imageCount FROM ProductImage pi WHERE pi.product.seller.sellerId = :sellerId GROUP BY pi.product.seller")
    List<Object[]> findProductImageCountBySeller(@Param("sellerId") Integer sellerId);
    
    /**
     * Find product images with product count by product
     */
    @Query("SELECT pi.product, COUNT(pi) as imageCount FROM ProductImage pi WHERE pi.product.productId = :productId GROUP BY pi.product")
    List<Object[]> findProductImageCountByProduct(@Param("productId") Integer productId);
    
    /**
     * Find product images with primary image count by product
     */
    @Query("SELECT pi.product, COUNT(pi) as primaryImageCount FROM ProductImage pi WHERE pi.product.productId = :productId AND pi.isPrimary = true GROUP BY pi.product")
    List<Object[]> findPrimaryImageCountByProduct(@Param("productId") Integer productId);
    
    /**
     * Find products without images
     */
    @Query("SELECT p FROM Product p WHERE p NOT IN (SELECT DISTINCT pi.product FROM ProductImage pi)")
    List<Object[]> findProductsWithoutImages();
    
    /**
     * Find products without primary images
     */
    @Query("SELECT p FROM Product p WHERE p NOT IN (SELECT DISTINCT pi.product FROM ProductImage pi WHERE pi.isPrimary = true)")
    List<Object[]> findProductsWithoutPrimaryImages();
    
    /**
     * Find product images by sort order with product details
     */
    @Query("SELECT pi, pi.product FROM ProductImage pi WHERE pi.sortOrder BETWEEN :minSortOrder AND :maxSortOrder ORDER BY pi.sortOrder ASC")
    List<Object[]> findProductImagesBySortOrderWithProduct(@Param("minSortOrder") Integer minSortOrder, 
                                                          @Param("maxSortOrder") Integer maxSortOrder);
    
    /**
     * Find product images by alt text with product details
     */
    @Query("SELECT pi, pi.product FROM ProductImage pi WHERE LOWER(pi.altText) LIKE LOWER(CONCAT('%', :altText, '%')) ORDER BY pi.product.name ASC, pi.sortOrder ASC")
    List<Object[]> findProductImagesByAltTextWithProduct(@Param("altText") String altText);
    
    /**
     * Find product images by image URL with product details
     */
    @Query("SELECT pi, pi.product FROM ProductImage pi WHERE LOWER(pi.imageUrl) LIKE LOWER(CONCAT('%', :imageUrl, '%')) ORDER BY pi.product.name ASC, pi.sortOrder ASC")
    List<Object[]> findProductImagesByImageUrlWithProduct(@Param("imageUrl") String imageUrl);
} 