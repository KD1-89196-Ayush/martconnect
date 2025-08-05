package com.sunbeam.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sunbeam.dto.ProductDto;
import com.sunbeam.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        try {
            List<ProductDto> products = productService.findAll();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Integer id) {
        try {
            Optional<ProductDto> product = productService.findById(id);
            if (product.isPresent()) {
                return ResponseEntity.ok(product.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<ProductDto> addProduct(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("Received product request: " + request);
            
            // Extract product data from request
            String name = (String) request.get("name");
            String description = (String) request.get("description");
            BigDecimal price = new BigDecimal(request.get("price").toString());
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
                // Try both sellerId and seller_id to handle frontend variations
                sellerId = (Integer) ((Map<?, ?>) sellerObj).get("sellerId");
                if (sellerId == null) {
                    sellerId = (Integer) ((Map<?, ?>) sellerObj).get("seller_id");
                }
            } else if (sellerObj instanceof Integer) {
                sellerId = (Integer) sellerObj;
            }
            
            if (name == null || price == null || unit == null || stock == null) {
                return ResponseEntity.badRequest().build();
            }
            
            // Create product DTO
            ProductDto productDto = new ProductDto();
            productDto.setName(name);
            productDto.setDescription(description);
            productDto.setPrice(price);
            productDto.setUnit(unit);
            productDto.setStock(stock);
            productDto.setImageUrl(imageUrl);
            productDto.setCategoryId(categoryId);
            productDto.setSellerId(sellerId);
            
            ProductDto createdProduct = productService.addProduct(productDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (Exception e) {
            System.err.println("Error creating product: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Integer id, @RequestBody ProductDto productDto) {
        try {
            ProductDto updatedProduct = productService.updateProduct(id, productDto);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductDto>> getProductsBySeller(@PathVariable Integer sellerId) {
        try {
            List<ProductDto> products = productService.findBySeller(sellerId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 