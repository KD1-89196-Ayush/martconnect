package com.sunbeam.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.sunbeam.entities.ProductImage;

/**
 * Service interface for ProductImage entity operations
 */
public interface ProductImageService extends BaseService<ProductImage, Integer> {
    
    /**
     * Find product images by product
     */
    List<ProductImage> findByProduct(Integer productId);
    
    /**
     * Find product images by product and primary status
     */
    List<ProductImage> findByProductAndPrimary(Integer productId, Boolean isPrimary);
    
    /**
     * Find primary product image by product
     */
    Optional<ProductImage> findPrimaryByProduct(Integer productId);
    
    /**
     * Find product images by image URL
     */
    List<ProductImage> findByImageUrl(String imageUrl);
    
    /**
     * Find product images by alt text
     */
    List<ProductImage> findByAltText(String altText);
    
    /**
     * Find product images by alt text or image URL containing search term
     */
    List<ProductImage> findByAltTextOrImageUrlContaining(String searchTerm);
    
    /**
     * Find product images by sort order range
     */
    List<ProductImage> findBySortOrderRange(Integer minSortOrder, Integer maxSortOrder);
    
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
    List<ProductImage> findByProductAndSortOrderRange(Integer productId, Integer minSortOrder, Integer maxSortOrder);
    
    /**
     * Find product images by primary status
     */
    List<ProductImage> findByPrimaryStatus(Boolean isPrimary);
    
    /**
     * Find product images by primary status and product
     */
    List<ProductImage> findByPrimaryStatusAndProduct(Boolean isPrimary, Integer productId);
    
    /**
     * Count product images by product
     */
    long countByProduct(Integer productId);
    
    /**
     * Count product images by product and primary status
     */
    long countByProductAndPrimary(Integer productId, Boolean isPrimary);
    
    /**
     * Count product images by primary status
     */
    long countByPrimaryStatus(Boolean isPrimary);
    
    /**
     * Find product images created in a specific date range
     */
    List<ProductImage> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find product images by seller
     */
    List<ProductImage> findBySeller(Integer sellerId);
    
    /**
     * Find product images by seller email
     */
    List<ProductImage> findBySellerEmail(String sellerEmail);
    
    /**
     * Find product images by seller name
     */
    List<ProductImage> findBySellerName(String sellerName);
    
    /**
     * Find product images by product name
     */
    List<ProductImage> findByProductName(String productName);
    
    /**
     * Find product images by product category
     */
    List<ProductImage> findByProductCategory(String category);
    
    /**
     * Get product images by product and primary status with product details
     */
    List<Object[]> getProductImagesByProductAndPrimaryWithProduct(Integer productId, Boolean isPrimary);
    
    /**
     * Get product images by product with product details
     */
    List<Object[]> getProductImagesByProductWithProduct(Integer productId);
    
    /**
     * Get product images by seller with product details
     */
    List<Object[]> getProductImagesBySellerWithProduct(Integer sellerId);
    
    /**
     * Get product images by seller email with product details
     */
    List<Object[]> getProductImagesBySellerEmailWithProduct(String sellerEmail);
    
    /**
     * Get product images by seller name with product details
     */
    List<Object[]> getProductImagesBySellerNameWithProduct(String sellerName);
    
    /**
     * Get product images by product name with product details
     */
    List<Object[]> getProductImagesByProductNameWithProduct(String productName);
    
    /**
     * Get product images by product category with product details
     */
    List<Object[]> getProductImagesByProductCategoryWithProduct(String category);
    
    /**
     * Get product images with product count by seller
     */
    List<Object[]> getProductImageCountBySeller(Integer sellerId);
    
    /**
     * Get product images with product count by product
     */
    List<Object[]> getProductImageCountByProduct(Integer productId);
    
    /**
     * Get product images with primary image count by product
     */
    List<Object[]> getPrimaryImageCountByProduct(Integer productId);
    
    /**
     * Get products without images
     */
    List<Object[]> getProductsWithoutImages();
    
    /**
     * Get products without primary images
     */
    List<Object[]> getProductsWithoutPrimaryImages();
    
    /**
     * Get product images by sort order with product details
     */
    List<Object[]> getProductImagesBySortOrderWithProduct(Integer minSortOrder, Integer maxSortOrder);
    
    /**
     * Get product images by alt text with product details
     */
    List<Object[]> getProductImagesByAltTextWithProduct(String altText);
    
    /**
     * Get product images by image URL with product details
     */
    List<Object[]> getProductImagesByImageUrlWithProduct(String imageUrl);
    
    /**
     * Add product image
     */
    ProductImage addProductImage(ProductImage productImage);
    
    /**
     * Update product image
     */
    ProductImage updateProductImage(Integer imageId, ProductImage productImage);
    
    /**
     * Delete product image
     */
    void deleteProductImage(Integer imageId);
    
    /**
     * Set primary image
     */
    ProductImage setPrimaryImage(Integer imageId);
    
    /**
     * Update image sort order
     */
    ProductImage updateSortOrder(Integer imageId, Integer sortOrder);
    
    /**
     * Get product image statistics
     */
    Object[] getProductImageStatistics(Integer imageId);
    
    /**
     * Get product image analytics
     */
    Object[] getProductImageAnalytics();
    
    /**
     * Validate image URL
     */
    boolean validateImageUrl(String imageUrl);
    
    /**
     * Get image suggestions
     */
    List<String> getImageSuggestions(String searchTerm);
    
    /**
     * Bulk update image sort orders
     */
    void bulkUpdateSortOrders(List<Integer> imageIds, List<Integer> sortOrders);
    
    /**
     * Get image storage analytics
     */
    Object[] getImageStorageAnalytics();
} 