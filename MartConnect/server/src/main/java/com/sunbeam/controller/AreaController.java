package com.sunbeam.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunbeam.dto.AreaDto;
import com.sunbeam.service.AreaService;

@RestController
@RequestMapping("/api/areas")
public class AreaController {
    
    @Autowired
    private AreaService areaService;
    
    @PostMapping
    public ResponseEntity<AreaDto> addArea(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("Received area request: " + request);
            
            String areaName = (String) request.get("area_name");
            String city = (String) request.get("city");
            String state = (String) request.get("state");
            String pincode = (String) request.get("pincode");
            Integer customerId = (Integer) request.get("customer_id");
            
            System.out.println("Extracted data - areaName: " + areaName + ", city: " + city + ", state: " + state + ", pincode: " + pincode + ", customerId: " + customerId);
            
            if (areaName == null || areaName.trim().isEmpty()) {
                System.out.println("Area name is empty");
                return ResponseEntity.badRequest().body(null);
            }
            if (city == null || city.trim().isEmpty()) {
                System.out.println("City is empty");
                return ResponseEntity.badRequest().body(null);
            }
            if (state == null || state.trim().isEmpty()) {
                System.out.println("State is empty");
                return ResponseEntity.badRequest().body(null);
            }
            if (pincode == null || pincode.trim().isEmpty()) {
                System.out.println("Pincode is empty");
                return ResponseEntity.badRequest().body(null);
            }
            if (customerId == null) {
                System.out.println("Customer ID is null");
                return ResponseEntity.badRequest().body(null);
            }
            
            // Validate pincode format (6 digits)
            if (!pincode.matches("^[0-9]{6}$")) {
                System.out.println("Invalid pincode format: " + pincode);
                return ResponseEntity.badRequest().body(null);
            }
            
            AreaDto areaDto = new AreaDto();
            areaDto.setAreaName(areaName.trim());
            areaDto.setCity(city.trim());
            areaDto.setState(state.trim());
            areaDto.setPincode(pincode.trim());
            areaDto.setCustomerId(customerId);
            areaDto.setCreatedAt(LocalDateTime.now());
            
            System.out.println("Saving area: " + areaDto);
            AreaDto savedArea = areaService.addAreaForCustomer(customerId, areaDto);
            System.out.println("Area saved successfully: " + savedArea);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArea);
        } catch (RuntimeException e) {
            System.err.println("Runtime error saving area: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            System.err.println("Unexpected error saving area: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AreaDto>> getCustomerAreas(@PathVariable Integer customerId) {
        try {
            List<AreaDto> areas = areaService.findByCustomer(customerId);
            return ResponseEntity.ok(areas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 