package com.sunbeam.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.ProductImageDao;
import com.sunbeam.dao.ProductDao;
import com.sunbeam.entities.ProductImage;
import com.sunbeam.entities.Product;
import com.sunbeam.service.ProductImageService;

/**
 * Implementation of ProductImageService
 */
@Service
@Transactional
public class ProductImageServiceImpl implements ProductImageService {
    
    @Autowired
    private ProductImageDao productImageDao;
    
    @Autowired
    private ProductDao productDao;
    
    @Override
    public ProductImage save(ProductImage productImage) {
        return productImageDao.save(productImage);
    }
    
    @Override
    public Optional<ProductImage> findById(Integer id) {
        return productImageDao.findById(id);
    }
    
    @Override
    public List<ProductImage> findAll() {
        return productImageDao.findAll();
    }
    
    @Override
    public void deleteById(Integer id) {
        productImageDao.deleteById(id);
    }
    
    @Override
    public boolean existsById(Integer id) {
        return productImageDao.existsById(id);
    }
    
    @Override
    public long count() {
        return productImageDao.count();
    }
    
    @Override
    public List<ProductImage> findByProduct(Integer productId) {
        return productImageDao.findByProduct_ProductId(productId);
    }
    
    @Override
    public List<ProductImage> findByProductAndPrimary(Integer productId, Boolean isPrimary) {
        return productImageDao.findByIsPrimaryAndProduct_ProductId(isPrimary, productId);
    }
    
    @Override
    public Optional<ProductImage> findPrimaryByProduct(Integer productId) {
        return productImageDao.findByProduct_ProductIdAndIsPrimaryTrue(productId);
    }
    
    @Override
    public List<ProductImage> findByImageUrl(String imageUrl) {
        return productImageDao.findByImageUrlContaining(imageUrl);
    }
    
    @Override
    public List<ProductImage> findByAltText(String altText) {
        return productImageDao.findByAltTextContainingIgnoreCase(altText);
    }
    
    @Override
    public List<ProductImage> findByAltTextOrImageUrlContaining(String searchTerm) {
        return productImageDao.findByAltTextOrImageUrlContaining(searchTerm);
    }
    
    @Override
    public List<ProductImage> findBySortOrderRange(Integer minSortOrder, Integer maxSortOrder) {
        return productImageDao.findBySortOrderBetween(minSortOrder, maxSortOrder);
    }
    
    @Override
    public List<ProductImage> findBySortOrderGreaterThan(Integer minSortOrder) {
        return productImageDao.findBySortOrderGreaterThan(minSortOrder);
    }
    
    @Override
    public List<ProductImage> findBySortOrderLessThanEqual(Integer maxSortOrder) {
        return productImageDao.findBySortOrderLessThanEqual(maxSortOrder);
    }
    
    @Override
    public List<ProductImage> findByProductAndSortOrderRange(Integer productId, Integer minSortOrder, Integer maxSortOrder) {
        return productImageDao.findByProduct_ProductIdAndSortOrderBetween(productId, minSortOrder, maxSortOrder);
    }
    
    @Override
    public List<ProductImage> findByPrimaryStatus(Boolean isPrimary) {
        return productImageDao.findByIsPrimary(isPrimary);
    }
    
    @Override
    public List<ProductImage> findByPrimaryStatusAndProduct(Boolean isPrimary, Integer productId) {
        return productImageDao.findByIsPrimaryAndProduct_ProductId(isPrimary, productId);
    }
    
    @Override
    public long countByProduct(Integer productId) {
        return productImageDao.countByProduct_ProductId(productId);
    }
    
    @Override
    public long countByProductAndPrimary(Integer productId, Boolean isPrimary) {
        return productImageDao.countByProduct_ProductIdAndIsPrimary(productId, isPrimary);
    }
    
    @Override
    public long countByPrimaryStatus(Boolean isPrimary) {
        return productImageDao.countByIsPrimary(isPrimary);
    }
    
    @Override
    public List<ProductImage> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return productImageDao.findByCreatedAtBetween(startDate, endDate);
    }
    
    @Override
    public List<ProductImage> findBySeller(Integer sellerId) {
        return productImageDao.findBySellerId(sellerId);
    }
    
    @Override
    public List<ProductImage> findBySellerEmail(String sellerEmail) {
        return productImageDao.findBySellerEmail(sellerEmail);
    }
    
    @Override
    public List<ProductImage> findBySellerName(String sellerName) {
        return productImageDao.findBySellerName(sellerName);
    }
    
    @Override
    public List<ProductImage> findByProductName(String productName) {
        return productImageDao.findByProductName(productName);
    }
    
    @Override
    public List<ProductImage> findByProductCategory(String category) {
        return productImageDao.findByProductCategory(category);
    }
    
    @Override
    public List<Object[]> getProductImagesByProductAndPrimaryWithProduct(Integer productId, Boolean isPrimary) {
        return productImageDao.findProductImagesByProductAndPrimaryWithProduct(productId, isPrimary);
    }
    
    @Override
    public List<Object[]> getProductImagesByProductWithProduct(Integer productId) {
        return productImageDao.findProductImagesByProductWithProduct(productId);
    }
    
    @Override
    public List<Object[]> getProductImagesBySellerWithProduct(Integer sellerId) {
        return productImageDao.findProductImagesBySellerWithProduct(sellerId);
    }
    
    @Override
    public List<Object[]> getProductImagesBySellerEmailWithProduct(String sellerEmail) {
        return productImageDao.findProductImagesBySellerEmailWithProduct(sellerEmail);
    }
    
    @Override
    public List<Object[]> getProductImagesBySellerNameWithProduct(String sellerName) {
        return productImageDao.findProductImagesBySellerNameWithProduct(sellerName);
    }
    
    @Override
    public List<Object[]> getProductImagesByProductNameWithProduct(String productName) {
        return productImageDao.findProductImagesByProductNameWithProduct(productName);
    }
    
    @Override
    public List<Object[]> getProductImagesByProductCategoryWithProduct(String category) {
        return productImageDao.findProductImagesByProductCategoryWithProduct(category);
    }
    
    @Override
    public List<Object[]> getProductImageCountBySeller(Integer sellerId) {
        return productImageDao.findProductImageCountBySeller(sellerId);
    }
    
    @Override
    public List<Object[]> getProductImageCountByProduct(Integer productId) {
        return productImageDao.findProductImageCountByProduct(productId);
    }
    
    @Override
    public List<Object[]> getPrimaryImageCountByProduct(Integer productId) {
        return productImageDao.findPrimaryImageCountByProduct(productId);
    }
    
    @Override
    public List<Object[]> getProductsWithoutImages() {
        return productImageDao.findProductsWithoutImages();
    }
    
    @Override
    public List<Object[]> getProductsWithoutPrimaryImages() {
        return productImageDao.findProductsWithoutPrimaryImages();
    }
    
    @Override
    public List<Object[]> getProductImagesBySortOrderWithProduct(Integer minSortOrder, Integer maxSortOrder) {
        return productImageDao.findProductImagesBySortOrderWithProduct(minSortOrder, maxSortOrder);
    }
    
    @Override
    public List<Object[]> getProductImagesByAltTextWithProduct(String altText) {
        return productImageDao.findProductImagesByAltTextWithProduct(altText);
    }
    
    @Override
    public List<Object[]> getProductImagesByImageUrlWithProduct(String imageUrl) {
        return productImageDao.findProductImagesByImageUrlWithProduct(imageUrl);
    }
    
    @Override
    public ProductImage addProductImage(ProductImage productImage) {
        // Validate product exists
        Optional<Product> productOpt = productDao.findById(productImage.getProduct().getProductId());
        if (productOpt.isEmpty()) {
            throw new RuntimeException("Product not found with ID: " + productImage.getProduct().getProductId());
        }
        
        // Validate image URL
        if (!validateImageUrl(productImage.getImageUrl())) {
            throw new RuntimeException("Invalid image URL: " + productImage.getImageUrl());
        }
        
        // Set timestamps
        productImage.setCreatedAt(LocalDateTime.now());
        
        // If this is the first image for the product, make it primary
        if (productImage.getIsPrimary() == null) {
            long existingImageCount = countByProduct(productImage.getProduct().getProductId());
            productImage.setIsPrimary(existingImageCount == 0);
        }
        
        // If setting as primary, remove primary status from other images
        if (Boolean.TRUE.equals(productImage.getIsPrimary())) {
            List<ProductImage> existingPrimaryImages = findByProductAndPrimary(productImage.getProduct().getProductId(), true);
            for (ProductImage existingImage : existingPrimaryImages) {
                existingImage.setIsPrimary(false);
                productImageDao.save(existingImage);
            }
        }
        
        return productImageDao.save(productImage);
    }
    
    @Override
    public ProductImage updateProductImage(Integer imageId, ProductImage productImage) {
        Optional<ProductImage> existingImage = findById(imageId);
        if (existingImage.isEmpty()) {
            throw new RuntimeException("Product image not found with ID: " + imageId);
        }
        
        ProductImage existing = existingImage.get();
        
        // Update fields if provided
        if (productImage.getImageUrl() != null) {
            if (!validateImageUrl(productImage.getImageUrl())) {
                throw new RuntimeException("Invalid image URL: " + productImage.getImageUrl());
            }
            existing.setImageUrl(productImage.getImageUrl());
        }
        
        if (productImage.getAltText() != null) {
            existing.setAltText(productImage.getAltText());
        }
        
        if (productImage.getSortOrder() != null) {
            existing.setSortOrder(productImage.getSortOrder());
        }
        
        if (productImage.getIsPrimary() != null) {
            // If setting as primary, remove primary status from other images
            if (Boolean.TRUE.equals(productImage.getIsPrimary())) {
                List<ProductImage> existingPrimaryImages = findByProductAndPrimary(existing.getProduct().getProductId(), true);
                for (ProductImage existingPrimaryImage : existingPrimaryImages) {
                    if (!existingPrimaryImage.getImageId().equals(imageId)) {
                        existingPrimaryImage.setIsPrimary(false);
                        productImageDao.save(existingPrimaryImage);
                    }
                }
            }
            existing.setIsPrimary(productImage.getIsPrimary());
        }
        
        // Update timestamp
        
        return productImageDao.save(existing);
    }
    
    @Override
    public void deleteProductImage(Integer imageId) {
        Optional<ProductImage> imageOpt = findById(imageId);
        if (imageOpt.isEmpty()) {
            throw new RuntimeException("Product image not found with ID: " + imageId);
        }
        
        ProductImage image = imageOpt.get();
        
        // If this is the primary image, make another image primary if available
        if (Boolean.TRUE.equals(image.getIsPrimary())) {
            List<ProductImage> otherImages = findByProduct(image.getProduct().getProductId());
            otherImages.removeIf(img -> img.getImageId().equals(imageId));
            
            if (!otherImages.isEmpty()) {
                ProductImage newPrimary = otherImages.get(0);
                newPrimary.setIsPrimary(true);
                productImageDao.save(newPrimary);
            }
        }
        
        productImageDao.deleteById(imageId);
    }
    
    @Override
    public ProductImage setPrimaryImage(Integer imageId) {
        Optional<ProductImage> imageOpt = findById(imageId);
        if (imageOpt.isEmpty()) {
            throw new RuntimeException("Product image not found with ID: " + imageId);
        }
        
        ProductImage image = imageOpt.get();
        
        // Remove primary status from other images of the same product
        List<ProductImage> existingPrimaryImages = findByProductAndPrimary(image.getProduct().getProductId(), true);
        for (ProductImage existingImage : existingPrimaryImages) {
            if (!existingImage.getImageId().equals(imageId)) {
                existingImage.setIsPrimary(false);
                productImageDao.save(existingImage);
            }
        }
        
        // Set this image as primary
        image.setIsPrimary(true);
        
        return productImageDao.save(image);
    }
    
    @Override
    public ProductImage updateSortOrder(Integer imageId, Integer sortOrder) {
        Optional<ProductImage> imageOpt = findById(imageId);
        if (imageOpt.isEmpty()) {
            throw new RuntimeException("Product image not found with ID: " + imageId);
        }
        
        ProductImage image = imageOpt.get();
        image.setSortOrder(sortOrder);
        
        return productImageDao.save(image);
    }
    
    @Override
    public Object[] getProductImageStatistics(Integer imageId) {
        Optional<ProductImage> imageOpt = findById(imageId);
        if (imageOpt.isEmpty()) {
            return null;
        }
        
        ProductImage image = imageOpt.get();
        
        // Get total images for this product
        long totalImages = countByProduct(image.getProduct().getProductId());
        
        // Get primary image count for this product
        long primaryImageCount = countByProductAndPrimary(image.getProduct().getProductId(), true);
        
        return new Object[]{image, totalImages, primaryImageCount};
    }
    
    @Override
    public Object[] getProductImageAnalytics() {
        // This would need a custom implementation
        return new Object[]{};
    }
    
    @Override
    public boolean validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return false;
        }
        
        // Basic URL validation
        return imageUrl.matches("^https?://.*") || imageUrl.startsWith("/");
    }
    
    @Override
    public List<String> getImageSuggestions(String searchTerm) {
        return findByAltTextOrImageUrlContaining(searchTerm).stream()
                .map(ProductImage::getAltText)
                .filter(altText -> altText != null && !altText.trim().isEmpty())
                .toList();
    }
    
    @Override
    public void bulkUpdateSortOrders(List<Integer> imageIds, List<Integer> sortOrders) {
        if (imageIds.size() != sortOrders.size()) {
            throw new RuntimeException("Image IDs and sort orders lists must have the same size");
        }
        
        for (int i = 0; i < imageIds.size(); i++) {
            Integer imageId = imageIds.get(i);
            Integer sortOrder = sortOrders.get(i);
            
            Optional<ProductImage> imageOpt = findById(imageId);
            if (imageOpt.isPresent()) {
                ProductImage image = imageOpt.get();
                image.setSortOrder(sortOrder);
                productImageDao.save(image);
            }
        }
    }
    
    @Override
    public Object[] getImageStorageAnalytics() {
        // This would need a custom implementation
        return new Object[]{};
    }
} 