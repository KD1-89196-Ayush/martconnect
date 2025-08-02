package com.sunbeam.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.CustomerDao;
import com.sunbeam.dao.OrderDao;
import com.sunbeam.entities.Customer;
import com.sunbeam.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    
    @Autowired
    private CustomerDao customerDao;
    
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
    
    // Core: Customer registration with validation
    @Override
    public Customer registerCustomer(Customer customer) {
        if (customerDao.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("Customer with email " + customer.getEmail() + " already exists");
        }
        if (customerDao.existsByPhone(customer.getPhone())) {
            throw new RuntimeException("Customer with phone " + customer.getPhone() + " already exists");
        }
        
        customer.setCreatedAt(LocalDateTime.now());
        customer.setPassword(hashPassword(customer.getPassword()));
        return customerDao.save(customer);
    }
    
    // Core: Customer authentication
    @Override
    public Optional<Customer> authenticate(String email, String password) {
        Optional<Customer> customerOpt = customerDao.findByEmail(email);
        if (customerOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Customer customer = customerOpt.get();
        if (verifyPassword(password, customer.getPassword())) {
            return Optional.of(customer);
        }
        return Optional.empty();
    }
    
    // Core: Change password with validation
    @Override
    public boolean changePassword(Integer customerId, String oldPassword, String newPassword) {
        Optional<Customer> customerOpt = customerDao.findById(customerId);
        if (customerOpt.isEmpty()) {
            return false;
        }
        
        Customer customer = customerOpt.get();
        if (!verifyPassword(oldPassword, customer.getPassword())) {
            return false;
        }
        
        customer.setPassword(hashPassword(newPassword));
        customer.setUpdatedAt(LocalDateTime.now());
        customerDao.save(customer);
        return true;
    }
    
    // Core: Update customer profile
    @Override
    public Customer updateCustomerProfile(Integer customerId, Customer customer) {
        Optional<Customer> existingCustomer = customerDao.findById(customerId);
        if (existingCustomer.isEmpty()) {
            throw new RuntimeException("Customer not found with ID: " + customerId);
        }
        
        Customer existing = existingCustomer.get();
        if (customer.getFirstName() != null) existing.setFirstName(customer.getFirstName());
        if (customer.getLastName() != null) existing.setLastName(customer.getLastName());
        if (customer.getPhone() != null) existing.setPhone(customer.getPhone());
        
        existing.setUpdatedAt(LocalDateTime.now());
        return customerDao.save(existing);
    }
    
    // Core: Get customer statistics
    @Override
    public Object[] getCustomerStatistics(Integer customerId) {
        Optional<Customer> customerOpt = customerDao.findById(customerId);
        if (customerOpt.isEmpty()) {
            return null;
        }
        
        Customer customer = customerOpt.get();
        long orderCount = orderDao.countByCustomer_CustomerId(customerId);
        
        return new Object[]{customer, orderCount, java.math.BigDecimal.ZERO};
    }
    
    // Essential CRUD methods
    @Override
    public Customer save(Customer customer) { return customerDao.save(customer); }
    
    @Override
    public Optional<Customer> findById(Integer id) { return customerDao.findById(id); }
    
    @Override
    public List<Customer> findAll() { return customerDao.findAll(); }
    
    @Override
    public void deleteById(Integer id) { customerDao.deleteById(id); }
    
    @Override
    public boolean existsById(Integer id) { return customerDao.existsById(id); }
    
    @Override
    public long count() { return customerDao.count(); }
    
    // Essential search methods
    @Override
    public Optional<Customer> findByEmail(String email) { return customerDao.findByEmail(email); }
    
    @Override
    public Optional<Customer> findByPhone(String phone) { return customerDao.findByPhone(phone); }
    
    @Override
    public List<Customer> findByFirstNameOrLastNameContaining(String searchTerm) {
        return customerDao.findByFirstNameOrLastNameContaining(searchTerm);
    }
    
    @Override
    public List<Customer> findByCity(String city) { return customerDao.findByCity(city); }
    
    @Override
    public List<Customer> findByState(String state) { return customerDao.findByState(state); }
    
    @Override
    public List<Customer> findByPincode(String pincode) { return customerDao.findByPincode(pincode); }
    
    // Placeholder methods for interface compliance
    @Override
    public List<Customer> findByFirstName(String firstName) { return List.of(); }
    
    @Override
    public List<Customer> findByLastName(String lastName) { return List.of(); }
    
    @Override
    public boolean existsByEmail(String email) { return customerDao.existsByEmail(email); }
    
    @Override
    public boolean existsByPhone(String phone) { return customerDao.existsByPhone(phone); }
    
    @Override
    public long countTotalCustomers() { return customerDao.countTotalCustomers(); }
    
    @Override
    public List<Customer> findByCreatedAtBetween(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) { return List.of(); }
    
    @Override
    public List<Object[]> getTopCustomersByOrderCount(int limit) { return List.of(); }
    
    @Override
    public List<Object[]> getTopCustomersByTotalSpent(int limit) { return List.of(); }
} 