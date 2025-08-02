package com.sunbeam.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.SellerDao;
import com.sunbeam.dao.ProductDao;
import com.sunbeam.dao.OrderDao;
import com.sunbeam.entities.Seller;
import com.sunbeam.service.SellerService;

@Service
@Transactional
public class SellerServiceImpl implements SellerService {
    
    @Autowired
    private SellerDao sellerDao;
    
    @Autowired
    private ProductDao productDao;
    
    @Autowired
    private OrderDao orderDao;
    
    // Core: Password hashing utility
    private String hashPassword(String password) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    private boolean verifyPassword(String rawPassword, String hashedPassword) {
        return hashPassword(rawPassword).equals(hashedPassword);
    }
    
    // Core: Seller registration with validation
    @Override
    public Seller registerSeller(Seller seller) {
        if (sellerDao.existsByEmail(seller.getEmail())) {
            throw new RuntimeException("Seller with email " + seller.getEmail() + " already exists");
        }
        if (sellerDao.existsByPhone(seller.getPhone())) {
            throw new RuntimeException("Seller with phone " + seller.getPhone() + " already exists");
        }
        if (sellerDao.existsByShopName(seller.getShopName())) {
            throw new RuntimeException("Shop name " + seller.getShopName() + " is already taken");
        }
        
        seller.setCreatedAt(LocalDateTime.now());
        seller.setPassword(hashPassword(seller.getPassword()));
        return sellerDao.save(seller);
    }
    
    // Core: Seller authentication
    @Override
    public Optional<Seller> authenticate(String email, String password) {
        Optional<Seller> sellerOpt = sellerDao.findByEmail(email);
        if (sellerOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Seller seller = sellerOpt.get();
        if (verifyPassword(password, seller.getPassword())) {
            return Optional.of(seller);
        }
        return Optional.empty();
    }
    
    // Core: Change password with validation
    @Override
    public boolean changePassword(Integer sellerId, String oldPassword, String newPassword) {
        Optional<Seller> sellerOpt = sellerDao.findById(sellerId);
        if (sellerOpt.isEmpty()) {
            return false;
        }
        
        Seller seller = sellerOpt.get();
        if (!verifyPassword(oldPassword, seller.getPassword())) {
            return false;
        }
        
        seller.setPassword(hashPassword(newPassword));
        seller.setUpdatedAt(LocalDateTime.now());
        sellerDao.save(seller);
        return true;
    }
    
    // Core: Update seller profile
    @Override
    public Seller updateSellerProfile(Integer sellerId, Seller seller) {
        Optional<Seller> existingSeller = sellerDao.findById(sellerId);
        if (existingSeller.isEmpty()) {
            throw new RuntimeException("Seller not found with ID: " + sellerId);
        }
        
        Seller existing = existingSeller.get();
        if (seller.getFirstName() != null) existing.setFirstName(seller.getFirstName());
        if (seller.getLastName() != null) existing.setLastName(seller.getLastName());
        if (seller.getPhone() != null) existing.setPhone(seller.getPhone());
        if (seller.getShopName() != null) existing.setShopName(seller.getShopName());
        if (seller.getShopAddress() != null) existing.setShopAddress(seller.getShopAddress());
        
        existing.setUpdatedAt(LocalDateTime.now());
        return sellerDao.save(existing);
    }
    
    // Core: Get seller statistics
    @Override
    public Object[] getSellerStatistics(Integer sellerId) {
        Optional<Seller> sellerOpt = sellerDao.findById(sellerId);
        if (sellerOpt.isEmpty()) {
            return null;
        }
        
        Seller seller = sellerOpt.get();
        long productCount = productDao.countBySeller_SellerId(sellerId);
        long orderCount = orderDao.countBySeller_SellerId(sellerId);
        
        return new Object[]{seller, productCount, orderCount, java.math.BigDecimal.ZERO};
    }
    
    // Essential CRUD methods
    @Override
    public Seller save(Seller seller) { return sellerDao.save(seller); }
    
    @Override
    public Optional<Seller> findById(Integer id) { return sellerDao.findById(id); }
    
    @Override
    public List<Seller> findAll() { return sellerDao.findAll(); }
    
    @Override
    public void deleteById(Integer id) { sellerDao.deleteById(id); }
    
    @Override
    public boolean existsById(Integer id) { return sellerDao.existsById(id); }
    
    @Override
    public long count() { return sellerDao.count(); }
    
    // Essential search methods
    @Override
    public Optional<Seller> findByEmail(String email) { return sellerDao.findByEmail(email); }
    
    @Override
    public Optional<Seller> findByPhone(String phone) { return sellerDao.findByPhone(phone); }
    
    @Override
    public List<Seller> findByShopName(String shopName) { return sellerDao.findByShopNameContainingIgnoreCase(shopName); }
    
    @Override
    public List<Seller> findByFirstNameOrLastNameOrShopNameContaining(String searchTerm) {
        return sellerDao.findByFirstNameOrLastNameOrShopNameContaining(searchTerm);
    }
    
    @Override
    public List<Seller> findByCity(String city) { return sellerDao.findByCity(city); }
    
    @Override
    public List<Seller> findByState(String state) { return sellerDao.findByState(state); }
    
    // Placeholder methods for interface compliance
    @Override
    public List<Seller> findByFirstName(String firstName) { return List.of(); }
    
    @Override
    public List<Seller> findByLastName(String lastName) { return List.of(); }
    
    @Override
    public boolean existsByEmail(String email) { return sellerDao.existsByEmail(email); }
    
    @Override
    public boolean existsByPhone(String phone) { return sellerDao.existsByPhone(phone); }
    
    @Override
    public boolean existsByShopName(String shopName) { return sellerDao.existsByShopName(shopName); }
    
    @Override
    public long countTotalSellers() { return sellerDao.countTotalSellers(); }
    
    @Override
    public List<Seller> findByCreatedAtBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) { return List.of(); }
    
    @Override
    public List<Object[]> getTopSellersByProductCount(int limit) { return List.of(); }
    
    @Override
    public List<Object[]> getTopSellersByOrderCount(int limit) { return List.of(); }
    
    @Override
    public List<Object[]> getTopSellersByRevenue(int limit) { return List.of(); }
    
    @Override
    public List<Seller> getSellersWithLowStockProducts(Integer lowStockThreshold) { return List.of(); }
    
    @Override
    public List<Seller> getSellersWithOutOfStockProducts() { return List.of(); }
} 