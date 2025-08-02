package com.sunbeam.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunbeam.dto.ApiResponse;
import com.sunbeam.entities.Area;
import com.sunbeam.entities.Customer;
import com.sunbeam.service.AreaService;

@RestController
@RequestMapping("/api/areas")
@CrossOrigin(origins = "*")
public class AreaController {
    
    @Autowired
    private AreaService areaService;
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse> getCustomerAreas(@PathVariable Integer customerId) {
        try {
            List<Area> areas = areaService.findByCustomer(customerId);
            return ResponseEntity.ok(ApiResponse.success("Customer areas retrieved successfully", areas));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve customer areas: " + e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse> addArea(@RequestBody Map<String, Object> request) {
        try {
            String areaName = (String) request.get("area_name");
            String city = (String) request.get("city");
            String state = (String) request.get("state");
            String pincode = (String) request.get("pincode");
            Integer customerId = (Integer) request.get("customer_id");
            
            if (areaName == null || areaName.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Area name is required"));
            }
            if (city == null || city.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("City is required"));
            }
            if (state == null || state.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("State is required"));
            }
            if (pincode == null || pincode.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Pincode is required"));
            }
            if (customerId == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Customer ID is required"));
            }
            
            Area area = new Area();
            area.setAreaName(areaName.trim());
            area.setCity(city.trim());
            area.setState(state.trim());
            area.setPincode(pincode.trim());
            
            // Set customer
            Customer customer = new Customer();
            customer.setCustomerId(customerId);
            area.setCustomer(customer);
            
            Area savedArea = areaService.addAreaForCustomer(customerId, area);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Area added successfully", savedArea));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to add area: " + e.getMessage()));
        }
    }
} 