package com.sunbeam.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sunbeam.dto.ApiResponse;
import com.sunbeam.entities.Category;
import com.sunbeam.entities.Product;
import com.sunbeam.entities.Seller;
import com.sunbeam.service.ProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping("/health")
    public ResponseEntity<ApiResponse> healthCheck() {
        try {
            System.out.println("Health check endpoint called");
            long productCount = productService.count();
            System.out.println("Database connection successful. Product count: " + productCount);
            return ResponseEntity.ok(ApiResponse.success("Backend is healthy", Map.of("productCount", productCount)));
        } catch (Exception e) {
            System.err.println("Health check failed: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Health check failed: " + e.getMessage()));
        }
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            System.out.println("Fetching all products...");
            // For now, return all products without pagination to simplify frontend
            List<Product> products = productService.findAll();
            System.out.println("Found " + products.size() + " products");
            
            return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", products));
        } catch (Exception e) {
            System.err.println("Error fetching products: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve products: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Integer id) {
        try {
            Optional<Product> product = productService.findById(id);
            if (product.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully", product.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Product not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve product: " + e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse> addProduct(@RequestBody Map<String, Object> request) {
        try {
            String name = (String) request.get("name");
            String description = (String) request.get("description");
            Object priceObj = request.get("price");
            BigDecimal price = null;
            if (priceObj != null) {
                price = new BigDecimal(priceObj.toString());
            }
            String unit = (String) request.get("unit");
            Integer stock = (Integer) request.get("stock");
            String imageUrl = (String) request.get("imageUrl");
            
            // Handle category
            Object categoryObj = request.get("category");
            Integer categoryId = null;
            if (categoryObj instanceof Map) {
                categoryId = (Integer) ((Map<?, ?>) categoryObj).get("categoryId");
            } else if (categoryObj instanceof Integer) {
                categoryId = (Integer) categoryObj;
            }
            
            // Handle seller
            Object sellerObj = request.get("seller");
            Integer sellerId = null;
            if (sellerObj instanceof Map) {
                sellerId = (Integer) ((Map<?, ?>) sellerObj).get("sellerId");
            } else if (sellerObj instanceof Integer) {
                sellerId = (Integer) sellerObj;
            }
            
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Product name is required"));
            }
            if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Valid price is required"));
            }
            if (unit == null || unit.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Unit is required"));
            }
            if (stock == null || stock < 0) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Valid stock quantity is required"));
            }
            if (categoryId == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Category ID is required"));
            }
            if (sellerId == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Seller ID is required"));
            }
            
            Product product = new Product();
            product.setName(name.trim());
            if (description != null && !description.trim().isEmpty()) {
                product.setDescription(description.trim());
            }
            product.setPrice(price);
            product.setUnit(unit.trim());
            product.setStock(stock);
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                product.setImageUrl(imageUrl.trim());
            }
            
            // Set category and seller (these will be loaded by the service)
            Category category = new Category();
            category.setCategoryId(categoryId);
            product.setCategory(category);
            
            Seller seller = new Seller();
            seller.setSellerId(sellerId);
            product.setSeller(seller);
            
            Product savedProduct = productService.addProductWithCategory(product);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Product added successfully", savedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to add product: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateProduct(@PathVariable Integer id, @RequestBody Product product) {
        try {
            Product updatedProduct = productService.updateProductWithCategory(id, product);
            return ResponseEntity.ok(ApiResponse.success("Product updated successfully", updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update product: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/stock")
    public ResponseEntity<ApiResponse> updateStock(@PathVariable Integer id, @RequestParam Integer newStock) {
        try {
            Product updatedProduct = productService.updateStock(id, newStock);
            return ResponseEntity.ok(ApiResponse.success("Stock updated successfully", updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update stock: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/price")
    public ResponseEntity<ApiResponse> updatePrice(@PathVariable Integer id, @RequestParam BigDecimal newPrice) {
        try {
            Product updatedProduct = productService.updatePrice(id, newPrice);
            return ResponseEntity.ok(ApiResponse.success("Price updated successfully", updatedProduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update price: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteById(id);
            return ResponseEntity.ok(ApiResponse.success("Product deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to delete product: " + e.getMessage()));
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) String unit,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Product> products = productService.searchProducts(name, category, minPrice, maxPrice, minStock, unit);
            
            return ResponseEntity.ok(ApiResponse.success("Products found", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Search failed: " + e.getMessage()));
        }
    }
    
    @GetMapping("/category/{categoryName}")
    public ResponseEntity<ApiResponse> getProductsByCategory(
            @PathVariable String categoryName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> products = productService.findByCategory(categoryName, pageable);
            
            return ResponseEntity.ok(ApiResponse.success("Products by category retrieved", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve products by category: " + e.getMessage()));
        }
    }
    
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse> getProductsBySeller(
            @PathVariable Integer sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Product> products = productService.findBySeller(sellerId, pageable);
            
            return ResponseEntity.ok(ApiResponse.success("Products by seller retrieved", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve products by seller: " + e.getMessage()));
        }
    }
    
    @GetMapping("/low-stock")
    public ResponseEntity<ApiResponse> getLowStockProducts(@RequestParam(defaultValue = "10") Integer threshold) {
        try {
            List<Product> products = productService.findProductsWithLowStock(threshold);
            return ResponseEntity.ok(ApiResponse.success("Low stock products retrieved", products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve low stock products: " + e.getMessage()));
        }
    }
} 