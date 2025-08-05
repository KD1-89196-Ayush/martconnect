package com.sunbeam.controller;

import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RestController;

import com.sunbeam.dto.CartDto;
import com.sunbeam.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CartDto>> getCustomerCart(@PathVariable Integer customerId) {
        try {
            List<CartDto> cartItems = cartService.findByCustomer(customerId);
            return ResponseEntity.ok(cartItems);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<CartDto> addToCart(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("Received cart request: " + request);
            
            // Extract cart data from request
            Integer productId = (Integer) request.get("productId");
            Integer customerId = (Integer) request.get("customerId");
            Integer quantity = (Integer) request.get("quantity");
            
            if (productId == null || customerId == null || quantity == null) {
                return ResponseEntity.badRequest().build();
            }
            
            // Create cart DTO
            CartDto cartDto = new CartDto();
            cartDto.setProductId(productId);
            cartDto.setCustomerId(customerId);
            cartDto.setQuantity(quantity);
            
            CartDto addedCartItem = cartService.addToCart(cartDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(addedCartItem);
        } catch (Exception e) {
            System.err.println("Error adding to cart: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/{cartId}/quantity")
    public ResponseEntity<CartDto> updateQuantity(@PathVariable Integer cartId, @RequestBody Map<String, Object> request) {
        try {
            Integer newQuantity = (Integer) request.get("quantity");
            if (newQuantity == null) {
                return ResponseEntity.badRequest().build();
            }
            
            CartDto updatedCartItem = cartService.updateQuantity(cartId, newQuantity);
            if (updatedCartItem == null) {
                // Item was removed due to quantity <= 0
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(updatedCartItem);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Integer cartId) {
        try {
            cartService.removeFromCart(cartId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @DeleteMapping("/customer/{customerId}")
    public ResponseEntity<Void> clearCustomerCart(@PathVariable Integer customerId) {
        try {
            cartService.clearCustomerCart(customerId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
} 