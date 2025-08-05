package com.sunbeam.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunbeam.dto.AuthResp;
import com.sunbeam.dto.CustomerDto;
import com.sunbeam.dto.SellerDto;
import com.sunbeam.dto.SignInDTO;
import com.sunbeam.security.JwtUtils;
import com.sunbeam.service.CustomerService;
import com.sunbeam.service.SellerService;

@RestController
@RequestMapping("/api/users")
public class AuthController {
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private SellerService sellerService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResp> login(@RequestBody SignInDTO signInDTO) {
        try {
            System.out.println("Login attempt for email: " + signInDTO.getEmail() + ", role: " + signInDTO.getRole());
            
            // Check if user exists first
            boolean userExists = false;
            if ("Seller".equals(signInDTO.getRole())) {
                var sellerOpt = sellerService.findByEmail(signInDTO.getEmail());
                userExists = sellerOpt.isPresent();
                if (userExists) {
                    System.out.println("Seller found with email: " + signInDTO.getEmail());
                }
            } else {
                var customerOpt = customerService.findByEmail(signInDTO.getEmail());
                userExists = customerOpt.isPresent();
                if (userExists) {
                    System.out.println("Customer found with email: " + signInDTO.getEmail());
                }
            }
            
            if (!userExists) {
                System.out.println("User not found with email: " + signInDTO.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new AuthResp("User not found", null, null));
            }
            
            // Authenticate user using Spring Security
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInDTO.getEmail(), signInDTO.getPassword())
            );
            
            System.out.println("Authentication successful for: " + signInDTO.getEmail());
            
            // Generate JWT token
            String jwtToken = jwtUtils.generateJwtToken(authentication);
            
            Object userData = null;
            
            if ("Seller".equals(signInDTO.getRole())) {
                // Seller login
                var sellerOpt = sellerService.findByEmail(signInDTO.getEmail());
                if (sellerOpt.isPresent()) {
                    userData = sellerOpt.get();
                }
            } else {
                // Customer login
                var customerOpt = customerService.findByEmail(signInDTO.getEmail());
                if (customerOpt.isPresent()) {
                    userData = customerOpt.get();
                }
            }
            
            return ResponseEntity.ok(new AuthResp("Login successful", jwtToken, userData));
                    
        } catch (BadCredentialsException e) {
            System.err.println("Bad credentials for email: " + signInDTO.getEmail());
            System.err.println("Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResp("Invalid credentials", null, null));
        } catch (Exception e) {
            System.err.println("Login error for email: " + signInDTO.getEmail());
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResp("Login failed: " + e.getMessage(), null, null));
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<AuthResp> register(@RequestBody Map<String, Object> request) {
        try {
            String role = (String) request.get("role");
            
            if ("Seller".equals(role)) {
                // Seller registration
                SellerDto sellerDto = new SellerDto();
                sellerDto.setFirstName((String) request.get("firstName"));
                sellerDto.setLastName((String) request.get("lastName"));
                sellerDto.setEmail((String) request.get("email"));
                sellerDto.setPhone((String) request.get("phone"));
                sellerDto.setShopName((String) request.get("shopName"));
                sellerDto.setShopAddress((String) request.get("shopAddress"));
                
                sellerDto.setPassword((String) request.get("password"));
                if (sellerDto.getPassword() == null || sellerDto.getPassword().trim().isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(new AuthResp("Password is required", null, null));
                }
                SellerDto savedSeller = sellerService.registerSeller(sellerDto);
                return ResponseEntity.ok(new AuthResp("Seller registered successfully", null, savedSeller));
                
            } else {
                // Customer registration
                CustomerDto customerDto = new CustomerDto();
                customerDto.setFirstName((String) request.get("firstName"));
                customerDto.setLastName((String) request.get("lastName"));
                customerDto.setEmail((String) request.get("email"));
                customerDto.setPhone((String) request.get("phone"));
                customerDto.setAddress((String) request.get("address"));
                
                customerDto.setPassword((String) request.get("password"));
                if (customerDto.getPassword() == null || customerDto.getPassword().trim().isEmpty()) {
                    return ResponseEntity.badRequest()
                            .body(new AuthResp("Password is required", null, null));
                }
                CustomerDto savedCustomer = customerService.registerCustomer(customerDto);
                return ResponseEntity.ok(new AuthResp("Customer registered successfully", null, savedCustomer));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResp("Registration failed: " + e.getMessage(), null, null));
        }
    }
} 