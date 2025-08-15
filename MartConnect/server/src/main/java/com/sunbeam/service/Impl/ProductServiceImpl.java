package com.sunbeam.service.Impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.ProductDao;
import com.sunbeam.dao.SellerDao;
import com.sunbeam.dto.ProductDto;
import com.sunbeam.dto.CategoryDto;
import com.sunbeam.entities.Product;
import com.sunbeam.entities.Category;
import com.sunbeam.entities.Seller;
import com.sunbeam.service.CategoryService;
import com.sunbeam.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    
    @Autowired
    private ProductDao productDao;
    
    @Autowired
    private SellerDao sellerDao;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ModelMapper modelMapper;
    
    // Core: Add product with category management
    @Override
    public ProductDto addProductWithCategory(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        
        // Handle category by ID
        if (productDto.getCategoryId() != null) {
            Optional<CategoryDto> existingCategory = categoryService.findById(productDto.getCategoryId());
            if (existingCategory.isPresent()) {
                Category categoryEntity = modelMapper.map(existingCategory.get(), Category.class);
                product.setCategory(categoryEntity);
            } else {
                throw new RuntimeException("Category not found with ID: " + productDto.getCategoryId());
            }
        } else if (product.getCategory() != null && product.getCategory().getName() != null) {
            // Handle category if it's sent as a string
            String categoryName = product.getCategory().getName();
            Optional<CategoryDto> existingCategory = categoryService.findByName(categoryName);
            
            if (existingCategory.isPresent()) {
                Category categoryEntity = modelMapper.map(existingCategory.get(), Category.class);
                product.setCategory(categoryEntity);
            } else {
                // Create new category and save it first
                CategoryDto newCategoryDto = new CategoryDto();
                newCategoryDto.setName(categoryName);
                newCategoryDto.setDescription(categoryName + " products");
                CategoryDto savedCategoryDto = categoryService.addCategory(newCategoryDto);
                Category categoryEntity = modelMapper.map(savedCategoryDto, Category.class);
                product.setCategory(categoryEntity);
            }
        }
        
        // Handle seller by ID
        if (productDto.getSellerId() != null) {
            Optional<Seller> sellerOpt = sellerDao.findById(productDto.getSellerId());
            if (sellerOpt.isPresent()) {
                product.setSeller(sellerOpt.get());
            } else {
                throw new RuntimeException("Seller not found with ID: " + productDto.getSellerId());
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
        Product savedProduct = productDao.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }
    
    // Core: Add product with validation
    @Override
    public ProductDto addProduct(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        
        // Handle category by ID
        if (productDto.getCategoryId() != null) {
            Optional<CategoryDto> existingCategory = categoryService.findById(productDto.getCategoryId());
            if (existingCategory.isPresent()) {
                Category categoryEntity = modelMapper.map(existingCategory.get(), Category.class);
                product.setCategory(categoryEntity);
            } else {
                throw new RuntimeException("Category not found with ID: " + productDto.getCategoryId());
            }
        }
        
        // Handle seller by ID
        if (productDto.getSellerId() != null) {
            Optional<Seller> sellerOpt = sellerDao.findById(productDto.getSellerId());
            if (sellerOpt.isPresent()) {
                product.setSeller(sellerOpt.get());
            } else {
                throw new RuntimeException("Seller not found with ID: " + productDto.getSellerId());
            }
        }
        
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Product price must be greater than zero");
        }
        if (product.getStock() < 0) {
            throw new RuntimeException("Product stock cannot be negative");
        }
        
        product.setCreatedAt(LocalDateTime.now());
        Product savedProduct = productDao.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }
    
    // Core: Update product with category management
    @Override
    public ProductDto updateProductWithCategory(Integer productId, ProductDto productDto) {
        Optional<Product> existingProduct = productDao.findById(productId);
        if (existingProduct.isEmpty()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        Product existing = existingProduct.get();
        Product product = modelMapper.map(productDto, Product.class);
        
        // Handle category if it's sent as a string
        if (product.getCategory() != null && product.getCategory().getName() != null) {
            String categoryName = product.getCategory().getName();
            Optional<CategoryDto> existingCategory = categoryService.findByName(categoryName);
            
            if (existingCategory.isPresent()) {
                Category categoryEntity = modelMapper.map(existingCategory.get(), Category.class);
                existing.setCategory(categoryEntity);
            } else {
                // Create new category and save it first
                CategoryDto newCategoryDto = new CategoryDto();
                newCategoryDto.setName(categoryName);
                newCategoryDto.setDescription(categoryName + " products");
                CategoryDto savedCategoryDto = categoryService.addCategory(newCategoryDto);
                Category categoryEntity = modelMapper.map(savedCategoryDto, Category.class);
                existing.setCategory(categoryEntity);
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
        Product updatedProduct = productDao.save(existing);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }
    
    // Core: Update product stock
    @Override
    public ProductDto updateStock(Integer productId, Integer newStock) {
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
        Product updatedProduct = productDao.save(product);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }
    
    // Core: Update product price
    @Override
    public ProductDto updatePrice(Integer productId, BigDecimal newPrice) {
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
        Product updatedProduct = productDao.save(product);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }
    
    // Core: Update product
    @Override
    public ProductDto updateProduct(Integer productId, ProductDto productDto) {
        Optional<Product> existingProduct = productDao.findById(productId);
        if (existingProduct.isEmpty()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        Product existing = existingProduct.get();
        Product product = modelMapper.map(productDto, Product.class);
        
        if (product.getName() != null) existing.setName(product.getName());
        if (product.getDescription() != null) existing.setDescription(product.getDescription());
        if (product.getPrice() != null) existing.setPrice(product.getPrice());
        if (product.getUnit() != null) existing.setUnit(product.getUnit());
        if (product.getStock() != null) existing.setStock(product.getStock());
        if (product.getImageUrl() != null) existing.setImageUrl(product.getImageUrl());
        
        existing.setUpdatedAt(LocalDateTime.now());
        Product updatedProduct = productDao.save(existing);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }
    
    // Essential CRUD methods
    @Override
    public ProductDto save(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        
        // Handle category by ID
        if (productDto.getCategoryId() != null) {
            Optional<CategoryDto> existingCategory = categoryService.findById(productDto.getCategoryId());
            if (existingCategory.isPresent()) {
                Category categoryEntity = modelMapper.map(existingCategory.get(), Category.class);
                product.setCategory(categoryEntity);
            } else {
                throw new RuntimeException("Category not found with ID: " + productDto.getCategoryId());
            }
        }
        
        // Handle seller by ID
        if (productDto.getSellerId() != null) {
            Optional<Seller> sellerOpt = sellerDao.findById(productDto.getSellerId());
            if (sellerOpt.isPresent()) {
                product.setSeller(sellerOpt.get());
            } else {
                throw new RuntimeException("Seller not found with ID: " + productDto.getSellerId());
            }
        }
        
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Product price must be greater than zero");
        }
        if (product.getStock() < 0) {
            throw new RuntimeException("Product stock cannot be negative");
        }
        product.setCreatedAt(LocalDateTime.now());
        Product savedProduct = productDao.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }
    
    @Override
    public Optional<ProductDto> findById(Integer id) { 
        return productDao.findById(id).map(product -> modelMapper.map(product, ProductDto.class)); 
    }
    
    @Override
    public List<ProductDto> findAll() { 
        return productDao.findAll().stream().map(product -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList()); 
    }
    
    @Override
    public void deleteById(Integer id) { productDao.deleteById(id); }
    
    @Override
    public boolean existsById(Integer id) { return productDao.existsById(id); }
    
    @Override
    public long count() { return productDao.count(); }
    
    // Essential search methods
    @Override
    public List<ProductDto> findByName(String name) { 
        return productDao.findByNameContainingIgnoreCase(name).stream().map(product -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList()); 
    }
    
    @Override
    public List<ProductDto> findByCategory(String categoryName) { 
        return productDao.findByCategory_NameContainingIgnoreCase(categoryName).stream().map(product -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList()); 
    }
    
    @Override
    public List<ProductDto> findBySeller(Integer sellerId) { 
        return productDao.findBySeller_SellerId(sellerId).stream().map(product -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList()); 
    }
       
    @Override
    public List<ProductDto> findByNameOrDescriptionContaining(String searchTerm) { 
        return productDao.findByNameOrDescriptionContaining(searchTerm).stream().map(product -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList()); 
    }
    
    @Override
    public List<ProductDto> searchProductsByNameAndCategory(String name, String category) {
        // Start with all products
        List<Product> products = productDao.findAll();
        
        // Filter by name if provided
        if (name != null && !name.trim().isEmpty()) {
            products = products.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()) ||
                           (p.getDescription() != null && p.getDescription().toLowerCase().contains(name.toLowerCase())))
                .toList();
        }
        
        // Filter by category if provided
        if (category != null && !category.trim().isEmpty()) {
            products = products.stream()
                .filter(p -> p.getCategory() != null && 
                           p.getCategory().getName().toLowerCase().contains(category.toLowerCase()))
                .toList();
        }
        
        return products.stream().map(product -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
    }
    
    @Override
    public List<ProductDto> searchProducts(String name, String category, BigDecimal minPrice, BigDecimal maxPrice, Integer minStock, String unit) {
        // Start with all products
        List<Product> products = productDao.findAll();
        
        // Filter by name if provided
        if (name != null && !name.trim().isEmpty()) {
            products = products.stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()) ||
                           (p.getDescription() != null && p.getDescription().toLowerCase().contains(name.toLowerCase())))
                .toList();
        }
        
        // Filter by category if provided
        if (category != null && !category.trim().isEmpty()) {
            products = products.stream()
                .filter(p -> p.getCategory() != null && 
                           p.getCategory().getName().toLowerCase().contains(category.toLowerCase()))
                .toList();
        }
        
        // Filter by price range if provided
        if (minPrice != null) {
            products = products.stream()
                .filter(p -> p.getPrice().compareTo(minPrice) >= 0)
                .toList();
        }
        
        if (maxPrice != null) {
            products = products.stream()
                .filter(p -> p.getPrice().compareTo(maxPrice) <= 0)
                .toList();
        }
        
        // Filter by minimum stock if provided
        if (minStock != null) {
            products = products.stream()
                .filter(p -> p.getStock() >= minStock)
                .toList();
        }
        
        // Filter by unit if provided
        if (unit != null && !unit.trim().isEmpty()) {
            products = products.stream()
                .filter(p -> p.getUnit().toLowerCase().contains(unit.toLowerCase()))
                .toList();
        }
        
        return products.stream().map(product -> modelMapper.map(product, ProductDto.class)).collect(Collectors.toList());
    }
    
    @Override
    public List<ProductDto> getProductsWithLowStockBySeller(Integer sellerId, Integer lowStockThreshold) {
        return productDao.findBySeller_SellerId(sellerId).stream()
            .filter(p -> p.getStock() <= lowStockThreshold)
            .map(product -> modelMapper.map(product, ProductDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getOutOfStockProductsBySeller(Integer sellerId) {
        return productDao.findBySeller_SellerId(sellerId).stream()
            .filter(p -> p.getStock() == 0)
            .map(product -> modelMapper.map(product, ProductDto.class))
            .collect(Collectors.toList());
    }

} 