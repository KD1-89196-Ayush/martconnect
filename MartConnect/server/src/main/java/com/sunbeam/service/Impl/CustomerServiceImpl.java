package com.sunbeam.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.CustomerDao;
import com.sunbeam.dao.OrderDao;
import com.sunbeam.dto.CustomerDto;
import com.sunbeam.entities.Customer;
import com.sunbeam.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    
    @Autowired
    private CustomerDao customerDao;
    
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ModelMapper modelMapper;
    
    // Core: Customer registration with validation
    @Override
    public CustomerDto registerCustomer(CustomerDto customerDto) {
        // Check if email already exists
        if (customerDao.existsByEmail(customerDto.getEmail())) {
            throw new RuntimeException("Customer with email " + customerDto.getEmail() + " already exists");
        }
        
        // Check if phone already exists
        if (customerDao.existsByPhone(customerDto.getPhone())) {
            throw new RuntimeException("Customer with phone " + customerDto.getPhone() + " already exists");
        }
        
        Customer customer = modelMapper.map(customerDto, Customer.class);
        
        // Encode password
        customer.setPassword(passwordEncoder.encode(customerDto.getPassword()));
        
        // Set timestamps
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        
        Customer savedCustomer = customerDao.save(customer);
        return modelMapper.map(savedCustomer, CustomerDto.class);
    }
    
    // Core: Customer authentication
    @Override
    public CustomerDto authenticate(String email, String password) {
        Optional<Customer> customerOpt = customerDao.findByEmail(email);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Customer not found with email: " + email);
        }
        
        Customer customer = customerOpt.get();
        
        // Verify password
        if (!passwordEncoder.matches(password, customer.getPassword())) {
            throw new RuntimeException("Invalid password for customer: " + email);
        }
        
        return modelMapper.map(customer, CustomerDto.class);
    }
    
    // Essential CRUD methods
    @Override
    public CustomerDto save(CustomerDto customerDto) { 
        Customer customer = modelMapper.map(customerDto, Customer.class);
        Customer savedCustomer = customerDao.save(customer);
        return modelMapper.map(savedCustomer, CustomerDto.class);
    }
    
    @Override
    public Optional<CustomerDto> findById(Integer id) { 
        return customerDao.findById(id).map(customer -> modelMapper.map(customer, CustomerDto.class)); 
    }
    
    @Override
    public List<CustomerDto> findAll() { 
        return customerDao.findAll().stream().map(customer -> modelMapper.map(customer, CustomerDto.class)).collect(Collectors.toList()); 
    }
    
    @Override
    public void deleteById(Integer id) { customerDao.deleteById(id); }
    
    @Override
    public boolean existsById(Integer id) { return customerDao.existsById(id); }
    
    @Override
    public long count() { return customerDao.count(); }
    
    @Override
    public Optional<CustomerDto> findByEmail(String email) { 
        return customerDao.findByEmail(email).map(customer -> modelMapper.map(customer, CustomerDto.class)); 
    }
    
    @Override
    public boolean existsByEmail(String email) { return customerDao.existsByEmail(email); }
    
    @Override
    public List<CustomerDto> findByFirstName(String firstName) { 
        return customerDao.findByFirstNameContainingIgnoreCase(firstName).stream().map(customer -> modelMapper.map(customer, CustomerDto.class)).collect(Collectors.toList()); 
    }
    
    @Override
    public List<CustomerDto> findByLastName(String lastName) { 
        return customerDao.findByLastNameContainingIgnoreCase(lastName).stream().map(customer -> modelMapper.map(customer, CustomerDto.class)).collect(Collectors.toList()); 
    }
    
    @Override
    public List<CustomerDto> findByPhone(String phone) { 
        // Since findByPhone was removed from CustomerDao, return empty list
        return List.of(); 
    }
    
    @Override
    public List<CustomerDto> findByAddressContaining(String address) { 
        // Since there's no direct address search, return empty list for now
        return List.of(); 
    }
    
    @Override
    public long countByFirstName(String firstName) { 
        return customerDao.findByFirstNameContainingIgnoreCase(firstName).size(); 
    }
    
    @Override
    public long countByLastName(String lastName) { 
        return customerDao.findByLastNameContainingIgnoreCase(lastName).size(); 
    }
    
    @Override
    public long countByPhone(String phone) { 
        // Since findByPhone was removed from CustomerDao, return 0
        return 0; 
    }
} 