package com.sunbeam.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.sunbeam.entities.Cart;
import com.sunbeam.service.CartService;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @PostMapping
    public ResponseEntity<ApiResponse> addToCart(@RequestBody Map<String, Object> request) {
        try {
            Integer customerId = (Integer) request.get("customerId");
            Integer productId = (Integer) request.get("productId");
            Integer quantity = (Integer) request.get("quantity");
            
            if (customerId == null || productId == null || quantity == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("customerId, productId, and quantity are required"));
            }
            
            Cart cartItem = cartService.addToCart(customerId, productId, quantity);
            return ResponseEntity.ok(ApiResponse.success("Item added to cart successfully", cartItem));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to add item to cart: " + e.getMessage()));
        }
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse> getCustomerCart(@PathVariable Integer customerId) {
        try {
            List<Cart> cartItems = cartService.findByCustomer(customerId);
            BigDecimal total = cartService.getCustomerCartTotal(customerId);
            long itemCount = cartService.getCustomerCartItemCount(customerId);
            
            Map<String, Object> cartData = Map.of(
                "items", cartItems,
                "total", total,
                "itemCount", itemCount
            );
            
            return ResponseEntity.ok(ApiResponse.success("Cart retrieved successfully", cartData));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve cart: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{cartId}/quantity")
    public ResponseEntity<ApiResponse> updateQuantity(
            @PathVariable Integer cartId,
            @RequestParam Integer newQuantity) {
        try {
            Cart updatedCart = cartService.updateCartItemQuantity(cartId, newQuantity);
            return ResponseEntity.ok(ApiResponse.success("Quantity updated successfully", updatedCart));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update quantity: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{cartId}")
    public ResponseEntity<ApiResponse> removeFromCart(@PathVariable Integer cartId) {
        try {
            cartService.removeFromCart(cartId);
            return ResponseEntity.ok(ApiResponse.success("Item removed from cart successfully"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to remove item from cart: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/customer/{customerId}/clear")
    public ResponseEntity<ApiResponse> clearCustomerCart(@PathVariable Integer customerId) {
        try {
            cartService.clearCustomerCart(customerId);
            return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully"));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to clear cart: " + e.getMessage()));
        }
    }
    
    @GetMapping("/customer/{customerId}/summary")
    public ResponseEntity<ApiResponse> getCartSummary(@PathVariable Integer customerId) {
        try {
            List<Cart> cartItems = cartService.findByCustomer(customerId);
            BigDecimal total = cartService.getCustomerCartTotal(customerId);
            long itemCount = cartService.getCustomerCartItemCount(customerId);
            
            Map<String, Object> summary = Map.of(
                "itemCount", itemCount,
                "total", total,
                "items", cartItems
            );
            
            return ResponseEntity.ok(ApiResponse.success("Cart summary retrieved", summary));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve cart summary: " + e.getMessage()));
        }
    }
    
    @GetMapping("/customer/{customerId}/validate")
    public ResponseEntity<ApiResponse> validateCartItems(@PathVariable Integer customerId) {
        try {
            List<Cart> cartItems = cartService.findCartItemsWithLowStock();
            List<Cart> customerLowStockItems = cartService.findCartItemsByCustomerWithLowStock(customerId);
            
            if (customerLowStockItems.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.success("Cart items are valid"));
            } else {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Some items in cart have insufficient stock"));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to validate cart: " + e.getMessage()));
        }
    }
} 