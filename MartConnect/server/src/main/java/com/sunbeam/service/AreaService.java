package com.sunbeam.service;

import java.util.List;
import java.util.Optional;

import com.sunbeam.dto.AreaDto;

/**
 * Service interface for Area entity operations
 */
public interface AreaService extends BaseService<AreaDto, Integer> {
    
    /**
     * Add area for customer
     */
    AreaDto addAreaForCustomer(Integer customerId, AreaDto areaDto);
    
    /**
     * Find areas by customer
     */
    List<AreaDto> findByCustomer(Integer customerId);
    
    /**
     * Find areas by customer and pincode
     */
    List<AreaDto> findByCustomerAndPincode(Integer customerId, String pincode);
    
    /**
     * Find areas by customer and city
     */
    List<AreaDto> findByCustomerAndCity(Integer customerId, String city);
    
    /**
     * Find areas by customer and state
     */
    List<AreaDto> findByCustomerAndState(Integer customerId, String state);
    
    /**
     * Count areas by customer
     */
    long countByCustomer(Integer customerId);
} 