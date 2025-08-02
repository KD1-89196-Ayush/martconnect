package com.sunbeam.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.ProductDao;
import com.sunbeam.entities.Product;
import com.sunbeam.entities.Category;
import com.sunbeam.service.ProductService;
import com.sunbeam.service.CategoryService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductDao productDao;
    
    @Autowired
    private CategoryService categoryService;
    
    // Core: Add product with category management
    @Override
    public Product addProductWithCategory(Product product) {
        // Handle category if it's sent as a string
        if (product.getCategory() != null && product.getCategory().getName() != null) {
            String categoryName = product.getCategory().getName();
            Optional<Category> existingCategory = categoryService.findByName(categoryName);
            
            if (existingCategory.isPresent()) {
                product.setCategory(existingCategory.get());
            } else {
                // Create new category and save it first
                Category newCategory = new Category();
                newCategory.setName(categoryName);
                newCategory.setDescription(categoryName + " products");
                Category savedCategory = categoryService.addCategory(newCategory);
                product.setCategory(savedCategory);
            }
        }
        
        // Validate product
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Product price must be greater than zero");
        }
        if (product.getStock() < 0) {
            throw new RuntimeException("Product stock cannot be negative");
        }
        
        product.setCreatedAt(LocalDateTime.now());
        return productDao.save(product);
    }
    
    // Core: Add product with validation
    @Override
    public Product addProduct(Product product) {
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Product price must be greater than zero");
        }
        if (product.getStock() < 0) {
            throw new RuntimeException("Product stock cannot be negative");
        }
        
        product.setCreatedAt(LocalDateTime.now());
        return productDao.save(product);
    }
    
    // Core: Update product with category management
    @Override
    public Product updateProductWithCategory(Integer productId, Product product) {
        Optional<Product> existingProduct = productDao.findById(productId);
        if (existingProduct.isEmpty()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        Product existing = existingProduct.get();
        
        // Handle category if it's sent as a string
        if (product.getCategory() != null && product.getCategory().getName() != null) {
            String categoryName = product.getCategory().getName();
            Optional<Category> existingCategory = categoryService.findByName(categoryName);
            
            if (existingCategory.isPresent()) {
                existing.setCategory(existingCategory.get());
            } else {
                // Create new category and save it first
                Category newCategory = new Category();
                newCategory.setName(categoryName);
                newCategory.setDescription(categoryName + " products");
                Category savedCategory = categoryService.addCategory(newCategory);
                existing.setCategory(savedCategory);
            }
        }
        
        // Update other fields
        if (product.getName() != null) existing.setName(product.getName());
        if (product.getDescription() != null) existing.setDescription(product.getDescription());
        if (product.getPrice() != null) existing.setPrice(product.getPrice());
        if (product.getUnit() != null) existing.setUnit(product.getUnit());
        if (product.getStock() != null) existing.setStock(product.getStock());
        if (product.getImageUrl() != null) existing.setImageUrl(product.getImageUrl());
        
        existing.setUpdatedAt(LocalDateTime.now());
        return productDao.save(existing);
    }
    
    // Core: Update product stock
    @Override
    public Product updateStock(Integer productId, Integer newStock) {
        Optional<Product> productOpt = productDao.findById(productId);
        if (productOpt.isEmpty()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        Product product = productOpt.get();
        if (newStock < 0) {
            throw new RuntimeException("Stock cannot be negative");
        }
        
        product.setStock(newStock);
        product.setUpdatedAt(LocalDateTime.now());
        return productDao.save(product);
    }
    
    // Core: Update product price
    @Override
    public Product updatePrice(Integer productId, BigDecimal newPrice) {
        Optional<Product> productOpt = productDao.findById(productId);
        if (productOpt.isEmpty()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        Product product = productOpt.get();
        if (newPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Price must be greater than zero");
        }
        
        product.setPrice(newPrice);
        product.setUpdatedAt(LocalDateTime.now());
        return productDao.save(product);
    }
    
    // Core: Update product
    @Override
    public Product updateProduct(Integer productId, Product product) {
        Optional<Product> existingProduct = productDao.findById(productId);
        if (existingProduct.isEmpty()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        Product existing = existingProduct.get();
        if (product.getName() != null) existing.setName(product.getName());
        if (product.getDescription() != null) existing.setDescription(product.getDescription());
        if (product.getPrice() != null) existing.setPrice(product.getPrice());
        if (product.getUnit() != null) existing.setUnit(product.getUnit());
        if (product.getStock() != null) existing.setStock(product.getStock());
        if (product.getImageUrl() != null) existing.setImageUrl(product.getImageUrl());
        
        existing.setUpdatedAt(LocalDateTime.now());
        return productDao.save(existing);
    }
    
    // Essential CRUD methods
    @Override
    public Product save(Product product) { return productDao.save(product); }
    
    @Override
    public Optional<Product> findById(Integer id) { 
        return productDao.findByIdWithCategoryAndSeller(id); 
    }
    
    @Override
    public List<Product> findAll() { 
        // Use a custom query to fetch products with their categories and sellers
        return productDao.findAllWithCategoryAndSeller(); 
    }
    
    @Override
    public void deleteById(Integer id) { productDao.deleteById(id); }
    
    @Override
    public boolean existsById(Integer id) { return productDao.existsById(id); }
    
    @Override
    public long count() { return productDao.count(); }
    
    // Essential search methods
    @Override
    public List<Product> findByName(String name) { return productDao.findByNameContainingIgnoreCase(name); }
    
    @Override
    public List<Product> findByCategory(String categoryName) { return productDao.findByCategory_NameContainingIgnoreCase(categoryName); }
    
    @Override
    public List<Product> findBySeller(Integer sellerId) { return productDao.findBySeller_SellerId(sellerId); }
    
    @Override
    public List<Product> findBySellerEmail(String sellerEmail) { return productDao.findBySeller_Email(sellerEmail); }
    
    @Override
    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) { return productDao.findByPriceBetween(minPrice, maxPrice); }
    
    @Override
    public List<Product> findByStockGreaterThan(Integer minStock) { return productDao.findByStockGreaterThan(minStock); }
    
    @Override
    public List<Product> findByStockLessThanEqual(Integer maxStock) { return productDao.findByStockLessThanEqual(maxStock); }
    
    @Override
    public List<Product> findByUnit(String unit) { return productDao.findByUnitContainingIgnoreCase(unit); }
    
    @Override
    public List<Product> findByNameOrDescriptionContaining(String searchTerm) { return productDao.findByNameOrDescriptionContaining(searchTerm); }
    
    @Override
    public List<Product> findByCategories(List<String> categories) { return productDao.findByCategories(categories); }
    
    @Override
    public List<Product> findBySellerAndCategory(Integer sellerId, String categoryName) { return productDao.findBySeller_SellerIdAndCategory_NameContainingIgnoreCase(sellerId, categoryName); }
    
    @Override
    public List<Product> findBySellerAndPriceRange(Integer sellerId, BigDecimal minPrice, BigDecimal maxPrice) { return productDao.findBySellerAndPriceRange(sellerId, minPrice, maxPrice); }
    
    @Override
    public Page<Product> findAll(Pageable pageable) { return productDao.findAll(pageable); }
    
    @Override
    public Page<Product> findByCategory(String categoryName, Pageable pageable) { return productDao.findByCategory_NameContainingIgnoreCase(categoryName, pageable); }
    
    @Override
    public Page<Product> findBySeller(Integer sellerId, Pageable pageable) { return productDao.findBySeller_SellerId(sellerId, pageable); }
    
    @Override
    public Page<Product> findByNameOrDescriptionContaining(String searchTerm, Pageable pageable) { return productDao.findByNameOrDescriptionContaining(searchTerm, pageable); }
    
    @Override
    public long countBySeller(Integer sellerId) { return productDao.countBySeller_SellerId(sellerId); }
    
    @Override
    public long countByCategory(String categoryName) { return productDao.countByCategory_NameContainingIgnoreCase(categoryName); }
    
    @Override
    public List<Product> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) { return productDao.findByCreatedAtBetween(startDate, endDate); }
    
    @Override
    public List<Product> findByUpdatedAtBetween(LocalDateTime startDate, LocalDateTime endDate) { return productDao.findByUpdatedAtBetween(startDate, endDate); }
    
    @Override
    public List<Product> findProductsWithLowStock(Integer lowStockThreshold) { return productDao.findProductsWithLowStock(lowStockThreshold); }
    
    @Override
    public List<Product> searchProducts(String searchTerm, String category, BigDecimal minPrice, BigDecimal maxPrice, Integer minStock, String unit) { return List.of(); }
    
    @Override
    public List<Product> getProductsWithLowStockBySeller(Integer sellerId, Integer lowStockThreshold) { return List.of(); }
    
    @Override
    public List<Product> getOutOfStockProductsBySeller(Integer sellerId) { return List.of(); }
} 