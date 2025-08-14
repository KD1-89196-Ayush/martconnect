package com.sunbeam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sunbeam.entities.Area;

@Repository
public interface AreaDao extends JpaRepository<Area, Integer> {
    
    /**
     * Find areas by customer
     */
    List<Area> findByCustomer_CustomerId(Integer customerId);
    
    /**
     * Find areas by customer and pincode
     */
    List<Area> findByCustomer_CustomerIdAndPincode(Integer customerId, String pincode);
    
    /**
     * Find areas by customer and city
     */
    List<Area> findByCustomer_CustomerIdAndCityContainingIgnoreCase(Integer customerId, String city);
    
    /**
     * Find areas by customer and state
     */
    List<Area> findByCustomer_CustomerIdAndStateContainingIgnoreCase(Integer customerId, String state);
    
    /**
     * Count areas by customer
     */
    long countByCustomer_CustomerId(Integer customerId);
} 