package com.sunbeam.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunbeam.dto.ApiResponse;
import com.sunbeam.dto.LoginDto;
import com.sunbeam.entities.Customer;
import com.sunbeam.entities.Seller;
import com.sunbeam.service.CustomerService;
import com.sunbeam.service.SellerService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private SellerService sellerService;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginDto loginDto) {
        try {
            if ("Seller".equals(loginDto.getRole())) {
                // Seller login
                var sellerOpt = sellerService.authenticate(loginDto.getEmail(), loginDto.getPassword());
                if (sellerOpt.isPresent()) {
                    Seller seller = sellerOpt.get();
                    Map<String, Object> data = new HashMap<>();
                    data.put("firstName", seller.getFirstName());
                    data.put("lastName", seller.getLastName());
                    data.put("token", "seller-token-" + seller.getSellerId());
                    data.put("seller_id", seller.getSellerId());
                    data.put("email", seller.getEmail());
                    data.put("role", "Seller");
                    data.put("shop_name", seller.getShopName());
                    data.put("shop_address", seller.getShopAddress());
                    
                    return ResponseEntity.ok(ApiResponse.success("Login successful", data));
                }
            } else {
                // Customer login
                var customerOpt = customerService.authenticate(loginDto.getEmail(), loginDto.getPassword());
                if (customerOpt.isPresent()) {
                    Customer customer = customerOpt.get();
                    Map<String, Object> data = new HashMap<>();
                    data.put("firstName", customer.getFirstName());
                    data.put("lastName", customer.getLastName());
                    data.put("token", "customer-token-" + customer.getCustomerId());
                    data.put("customer_id", customer.getCustomerId());
                    data.put("email", customer.getEmail());
                    data.put("phone", customer.getPhone());
                    data.put("address", customer.getAddress());
                    data.put("role", "User");
                    
                    return ResponseEntity.ok(ApiResponse.success("Login successful", data));
                }
            }
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid credentials"));
                    
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Login failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody Map<String, Object> request) {
        try {
            String role = (String) request.get("role");
            
            if ("Seller".equals(role)) {
                // Seller registration
                Seller seller = new Seller();
                seller.setFirstName((String) request.get("firstName"));
                seller.setLastName((String) request.get("lastName"));
                seller.setEmail((String) request.get("email"));
                seller.setPhone((String) request.get("phone"));
                seller.setPassword((String) request.get("password"));
                seller.setShopName((String) request.get("shopName"));
                seller.setShopAddress((String) request.get("shopAddress"));
                
                Seller savedSeller = sellerService.registerSeller(seller);
                return ResponseEntity.ok(ApiResponse.success("Seller registered successfully", savedSeller));
                
            } else {
                // Customer registration
                Customer customer = new Customer();
                customer.setFirstName((String) request.get("firstName"));
                customer.setLastName((String) request.get("lastName"));
                customer.setEmail((String) request.get("email"));
                customer.setPhone((String) request.get("phone"));
                customer.setPassword((String) request.get("password"));
                customer.setAddress((String) request.get("address"));
                
                Customer savedCustomer = customerService.registerCustomer(customer);
                return ResponseEntity.ok(ApiResponse.success("Customer registered successfully", savedCustomer));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Registration failed: " + e.getMessage()));
        }
    }
} 