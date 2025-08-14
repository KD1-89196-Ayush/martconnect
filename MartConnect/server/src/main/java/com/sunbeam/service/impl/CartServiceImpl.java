package com.sunbeam.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.CartDao;
import com.sunbeam.dao.CustomerDao;
import com.sunbeam.dao.ProductDao;
import com.sunbeam.dto.CartDto;
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
    
    @Autowired
    private ModelMapper modelMapper;
    
    // Essential CRUD methods
    @Override
    public CartDto save(CartDto cartDto) { 
        Cart cart = modelMapper.map(cartDto, Cart.class);
        Cart savedCart = cartDao.save(cart);
        return modelMapper.map(savedCart, CartDto.class);
    }
    
    @Override
    public Optional<CartDto> findById(Integer id) { 
        return cartDao.findById(id).map(cart -> modelMapper.map(cart, CartDto.class)); 
    }
    
    @Override
    public List<CartDto> findAll() { 
        return cartDao.findAll().stream().map(cart -> modelMapper.map(cart, CartDto.class)).collect(Collectors.toList()); 
    }
    
    @Override
    public void deleteById(Integer id) { cartDao.deleteById(id); }
    
    @Override
    public boolean existsById(Integer id) { return cartDao.existsById(id); }
    
    @Override
    public long count() { return cartDao.count(); }
    
    @Override
    public CartDto addToCart(CartDto cartDto) {
        // Validate customer exists
        Optional<Customer> customerOpt = customerDao.findById(cartDto.getCustomerId());
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Customer not found with ID: " + cartDto.getCustomerId());
        }
        
        // Validate product exists
        Optional<Product> productOpt = productDao.findById(cartDto.getProductId());
        if (productOpt.isEmpty()) {
            throw new RuntimeException("Product not found with ID: " + cartDto.getProductId());
        }
        
        Product product = productOpt.get();
        Customer customer = customerOpt.get();
        
        // Check if product is already in cart
        List<Cart> existingCartItems = cartDao.findByCustomer_CustomerIdAndProduct_ProductId(cartDto.getCustomerId(), cartDto.getProductId());
        if (!existingCartItems.isEmpty()) {
            // Update existing cart item
            Cart existingCart = existingCartItems.get(0);
            existingCart.setQuantity(existingCart.getQuantity() + cartDto.getQuantity());
            existingCart.setUpdatedAt(LocalDateTime.now());
            Cart updatedCart = cartDao.save(existingCart);
            return modelMapper.map(updatedCart, CartDto.class);
        }
        
        // Create new cart item
        Cart newCart = new Cart();
        newCart.setCustomer(customer);
        newCart.setProduct(product);
        newCart.setQuantity(cartDto.getQuantity());
        newCart.setCreatedAt(LocalDateTime.now());
        
        Cart savedCart = cartDao.save(newCart);
        return modelMapper.map(savedCart, CartDto.class);
    }
    
    @Override
    public CartDto updateQuantity(Integer cartId, Integer newQuantity) {
        Optional<Cart> cartOpt = cartDao.findById(cartId);
        if (cartOpt.isEmpty()) {
            throw new RuntimeException("Cart item not found with ID: " + cartId);
        }
        
        Cart cart = cartOpt.get();
        if (newQuantity <= 0) {
            // Remove item if quantity is 0 or negative
            cartDao.deleteById(cartId);
            return null;
        }
        
        cart.setQuantity(newQuantity);
        cart.setUpdatedAt(LocalDateTime.now());
        Cart updatedCart = cartDao.save(cart);
        return modelMapper.map(updatedCart, CartDto.class);
    }
    
    @Override
    public void removeFromCart(Integer cartId) {
        if (!cartDao.existsById(cartId)) {
            throw new RuntimeException("Cart item not found with ID: " + cartId);
        }
        cartDao.deleteById(cartId);
    }
    
    @Override
    public List<CartDto> findByCustomer(Integer customerId) {
        return cartDao.findByCustomer_CustomerId(customerId).stream()
                .map(cart -> modelMapper.map(cart, CartDto.class))
                .collect(Collectors.toList());
    }
} 