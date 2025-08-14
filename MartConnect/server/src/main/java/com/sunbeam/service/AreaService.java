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
    
} 