package com.sunbeam.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.sunbeam.entities.Area;

/**
 * Service interface for Area entity operations
 */
public interface AreaService extends BaseService<Area, Integer> {
    
    /**
     * Find areas by customer
     */
    List<Area> findByCustomer(Integer customerId);
    
    /**
     * Find areas by pincode
     */
    List<Area> findByPincode(String pincode);
    
    /**
     * Find areas by area name
     */
    List<Area> findByAreaName(String areaName);
    
    /**
     * Find areas by city
     */
    List<Area> findByCity(String city);
    
    /**
     * Find areas by state
     */
    List<Area> findByState(String state);
    
    /**
     * Find areas by customer and pincode
     */
    List<Area> findByCustomerAndPincode(Integer customerId, String pincode);
    
    /**
     * Find areas by customer and city
     */
    List<Area> findByCustomerAndCity(Integer customerId, String city);
    
    /**
     * Find areas by customer and state
     */
    List<Area> findByCustomerAndState(Integer customerId, String state);
    
    /**
     * Find areas by pincode and city
     */
    List<Area> findByPincodeAndCity(String pincode, String city);
    
    /**
     * Find areas by pincode and state
     */
    List<Area> findByPincodeAndState(String pincode, String state);
    
    /**
     * Find areas by city and state
     */
    List<Area> findByCityAndState(String city, String state);
    
    /**
     * Find areas by area name or city containing search term
     */
    List<Area> findByAreaNameOrCityContaining(String searchTerm);
    
    /**
     * Find areas by area name, city, or state containing search term
     */
    List<Area> findByAreaNameOrCityOrStateContaining(String searchTerm);
    
    /**
     * Check if area exists by pincode
     */
    boolean existsByPincode(String pincode);
    
    /**
     * Check if area exists by customer and pincode
     */
    boolean existsByCustomerAndPincode(Integer customerId, String pincode);
    
    /**
     * Count areas by customer
     */
    long countByCustomer(Integer customerId);
    
    /**
     * Count areas by pincode
     */
    long countByPincode(String pincode);
    
    /**
     * Count areas by city
     */
    long countByCity(String city);
    
    /**
     * Count areas by state
     */
    long countByState(String state);
    
    /**
     * Find areas created in a specific date range
     */
    List<Area> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find areas by customer email
     */
    List<Area> findByCustomerEmail(String email);
    
    /**
     * Find areas by customer name
     */
    List<Area> findByCustomerName(String name);
    
    /**
     * Get unique cities
     */
    List<String> getDistinctCities();
    
    /**
     * Get unique states
     */
    List<String> getDistinctStates();
    
    /**
     * Get unique pincodes
     */
    List<String> getDistinctPincodes();
    
    /**
     * Get areas by city with customer count
     */
    List<Object[]> getAreasByCityWithCustomerCount();
    
    /**
     * Get areas by state with customer count
     */
    List<Object[]> getAreasByStateWithCustomerCount();
    
    /**
     * Get areas by pincode with customer count
     */
    List<Object[]> getAreasByPincodeWithCustomerCount();
    
    /**
     * Get areas with customer details
     */
    List<Object[]> getAreasWithCustomerDetails();
    
    /**
     * Get areas by customer with full address
     */
    List<Object[]> getAreasByCustomerWithFullAddress(Integer customerId);
    
    /**
     * Get areas by city with full address
     */
    List<Object[]> getAreasByCityWithFullAddress(String city);
    
    /**
     * Get areas by state with full address
     */
    List<Object[]> getAreasByStateWithFullAddress(String state);
    
    /**
     * Add area for customer
     */
    Area addAreaForCustomer(Integer customerId, Area area);
    
    /**
     * Update area
     */
    Area updateArea(Integer areaId, Area area);
    
    /**
     * Remove area
     */
    void removeArea(Integer areaId);
    
    /**
     * Get customer primary area
     */
    Optional<Area> getCustomerPrimaryArea(Integer customerId);
    
    /**
     * Set customer primary area
     */
    Area setCustomerPrimaryArea(Integer customerId, Integer areaId);
    
    /**
     * Get area statistics
     */
    Object[] getAreaStatistics(Integer areaId);
    
    /**
     * Get location analytics
     */
    Object[] getLocationAnalytics();
    
    /**
     * Get popular cities
     */
    List<Object[]> getPopularCities(int limit);
    
    /**
     * Get popular states
     */
    List<Object[]> getPopularStates(int limit);
    
    /**
     * Get popular pincodes
     */
    List<Object[]> getPopularPincodes(int limit);
    
    /**
     * Validate pincode format
     */
    boolean validatePincodeFormat(String pincode);
    
    /**
     * Get delivery areas
     */
    List<Area> getDeliveryAreas(String city, String state);
} 