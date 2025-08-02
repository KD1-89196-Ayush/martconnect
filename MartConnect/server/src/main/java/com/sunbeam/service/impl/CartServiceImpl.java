package com.sunbeam.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.CartDao;
import com.sunbeam.dao.CustomerDao;
import com.sunbeam.dao.ProductDao;
import com.sunbeam.entities.Cart;
import com.sunbeam.entities.Customer;
import com.sunbeam.entities.Product;
import com.sunbeam.service.CartService;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartDao cartDao;
    
    @Autowired
    private ProductDao productDao;
    
    @Autowired
    private CustomerDao customerDao;
    
    // Core: Remove item from cart
    @Override
    public void removeFromCart(Integer cartId) {
        if (!cartDao.existsById(cartId)) {
            throw new RuntimeException("Cart item not found with ID: " + cartId);
        }
        cartDao.deleteById(cartId);
    }
    
    // Helper method for calculating cart total
    private BigDecimal calculateCartTotal(Integer customerId) {
        List<Cart> cartItems = cartDao.findByCustomer_CustomerId(customerId);
        return cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    // Essential CRUD methods
    @Override
    public Cart save(Cart cart) { return cartDao.save(cart); }
    
    @Override
    public Optional<Cart> findById(Integer id) { return cartDao.findById(id); }
    
    @Override
    public List<Cart> findAll() { return cartDao.findAll(); }
    
    @Override
    public void deleteById(Integer id) { cartDao.deleteById(id); }
    
    @Override
    public boolean existsById(Integer id) { return cartDao.existsById(id); }
    
    @Override
    public long count() { return cartDao.count(); }
    
    // Essential interface methods
    @Override
    public List<Cart> findByCustomer(Integer customerId) { return cartDao.findByCustomer_CustomerId(customerId); }
    
    @Override
    public List<Cart> findByProduct(Integer productId) { return cartDao.findByProduct_ProductId(productId); }
    
    @Override
    public List<Cart> findBySeller(Integer sellerId) { return cartDao.findBySellerId(sellerId); }
    
    @Override
    public List<Cart> findByCustomerAndProduct(Integer customerId, Integer productId) { return cartDao.findByCustomer_CustomerIdAndProduct_ProductId(customerId, productId); }
    
    @Override
    public long countByCustomer(Integer customerId) { return cartDao.countByCustomer_CustomerId(customerId); }
    
    @Override
    public long countByProduct(Integer productId) { return cartDao.countByProduct_ProductId(productId); }
    
    @Override
    public long countBySeller(Integer sellerId) { return cartDao.countBySellerId(sellerId); }
    
    @Override
    public List<Cart> findCartItemsWithLowStock() { return cartDao.findCartItemsWithLowStock(); }
    
    @Override
    public List<Cart> findCartItemsByCustomerWithLowStock(Integer customerId) { return cartDao.findCartItemsByCustomerWithLowStock(customerId); }
    
    @Override
    public Cart addToCart(Integer customerId, Integer productId, Integer quantity) {
        // Validate customer exists
        Optional<Customer> customerOpt = customerDao.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        
        // Validate product exists and has sufficient stock
        Optional<Product> productOpt = productDao.findById(productId);
        if (productOpt.isEmpty()) {
            throw new RuntimeException("Product not found with ID: " + productId);
        }
        
        Product product = productOpt.get();
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName() + ". Available: " + product.getStock());
        }
        
        // Check if item already exists in cart
        List<Cart> existingItems = cartDao.findByCustomer_CustomerIdAndProduct_ProductId(customerId, productId);
        if (!existingItems.isEmpty()) {
            // Update existing cart item
            Cart existingItem = existingItems.get(0);
            int newQuantity = existingItem.getQuantity() + quantity;
            if (product.getStock() < newQuantity) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName() + ". Available: " + product.getStock());
            }
            existingItem.setQuantity(newQuantity);
            existingItem.setUpdatedAt(LocalDateTime.now());
            return cartDao.save(existingItem);
        }
        
        // Create new cart item
        Cart cartItem = new Cart();
        cartItem.setCustomer(customerOpt.get());
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setCreatedAt(LocalDateTime.now());
        return cartDao.save(cartItem);
    }
    
    @Override
    public Cart updateCartItemQuantity(Integer cartId, Integer newQuantity) {
        Optional<Cart> cartOpt = cartDao.findById(cartId);
        if (cartOpt.isEmpty()) {
            throw new RuntimeException("Cart item not found with ID: " + cartId);
        }
        
        Cart cart = cartOpt.get();
        Optional<Product> productOpt = productDao.findById(cart.getProduct().getProductId());
        if (productOpt.isEmpty()) {
            throw new RuntimeException("Product not found");
        }
        
        Product product = productOpt.get();
        if (product.getStock() < newQuantity) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName() + ". Available: " + product.getStock());
        }
        
        cart.setQuantity(newQuantity);
        cart.setUpdatedAt(LocalDateTime.now());
        return cartDao.save(cart);
    }
    
    @Override
    public void clearCustomerCart(Integer customerId) {
        List<Cart> customerCart = cartDao.findByCustomer_CustomerId(customerId);
        cartDao.deleteAll(customerCart);
    }
    
    @Override
    public BigDecimal getCustomerCartTotal(Integer customerId) {
        return calculateCartTotal(customerId);
    }
    
    @Override
    public long getCustomerCartItemCount(Integer customerId) {
        return cartDao.countByCustomer_CustomerId(customerId);
    }
} 