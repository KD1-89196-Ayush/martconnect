package com.sunbeam.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
     * Find areas by pincode
     */
    List<Area> findByPincode(String pincode);
    
    /**
     * Find areas by area name
     */
    List<Area> findByAreaNameContainingIgnoreCase(String areaName);
    
    /**
     * Find areas by city
     */
    List<Area> findByCityContainingIgnoreCase(String city);
    
    /**
     * Find areas by state
     */
    List<Area> findByStateContainingIgnoreCase(String state);
    
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
     * Find areas by pincode and city
     */
    List<Area> findByPincodeAndCityContainingIgnoreCase(String pincode, String city);
    
    /**
     * Find areas by pincode and state
     */
    List<Area> findByPincodeAndStateContainingIgnoreCase(String pincode, String state);
    
    /**
     * Find areas by city and state
     */
    List<Area> findByCityContainingIgnoreCaseAndStateContainingIgnoreCase(String city, String state);
    
    /**
     * Find areas by area name or city containing search term
     */
    @Query("SELECT a FROM Area a WHERE LOWER(a.areaName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(a.city) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Area> findByAreaNameOrCityContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Find areas by area name, city, or state containing search term
     */
    @Query("SELECT a FROM Area a WHERE LOWER(a.areaName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(a.city) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(a.state) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Area> findByAreaNameOrCityOrStateContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Check if area exists by pincode
     */
    boolean existsByPincode(String pincode);
    
    /**
     * Check if area exists by customer and pincode
     */
    boolean existsByCustomer_CustomerIdAndPincode(Integer customerId, String pincode);
    
    /**
     * Count areas by customer
     */
    long countByCustomer_CustomerId(Integer customerId);
    
    /**
     * Count areas by pincode
     */
    long countByPincode(String pincode);
    
    /**
     * Count areas by city
     */
    long countByCityContainingIgnoreCase(String city);
    
    /**
     * Count areas by state
     */
    long countByStateContainingIgnoreCase(String state);
    
    /**
     * Find areas created in a specific date range
     */
    @Query("SELECT a FROM Area a WHERE a.createdAt BETWEEN :startDate AND :endDate")
    List<Area> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                     @Param("endDate") java.time.LocalDateTime endDate);
    
    /**
     * Find areas by customer email
     */
    @Query("SELECT a FROM Area a WHERE a.customer.email = :email")
    List<Area> findByCustomerEmail(@Param("email") String email);
    
    /**
     * Find areas by customer name (first name or last name)
     */
    @Query("SELECT a FROM Area a WHERE LOWER(a.customer.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(a.customer.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Area> findByCustomerName(@Param("name") String name);
    
    /**
     * Find unique cities
     */
    @Query("SELECT DISTINCT a.city FROM Area a ORDER BY a.city")
    List<String> findDistinctCities();
    
    /**
     * Find unique states
     */
    @Query("SELECT DISTINCT a.state FROM Area a ORDER BY a.state")
    List<String> findDistinctStates();
    
    /**
     * Find unique pincodes
     */
    @Query("SELECT DISTINCT a.pincode FROM Area a ORDER BY a.pincode")
    List<String> findDistinctPincodes();
    
    /**
     * Find areas by city with customer count
     */
    @Query("SELECT a.city, COUNT(a) as customerCount FROM Area a GROUP BY a.city ORDER BY customerCount DESC")
    List<Object[]> findAreasByCityWithCustomerCount();
    
    /**
     * Find areas by state with customer count
     */
    @Query("SELECT a.state, COUNT(a) as customerCount FROM Area a GROUP BY a.state ORDER BY customerCount DESC")
    List<Object[]> findAreasByStateWithCustomerCount();
    
    /**
     * Find areas by pincode with customer count
     */
    @Query("SELECT a.pincode, COUNT(a) as customerCount FROM Area a GROUP BY a.pincode ORDER BY customerCount DESC")
    List<Object[]> findAreasByPincodeWithCustomerCount();
    
    /**
     * Find areas with customer details
     */
    @Query("SELECT a, a.customer FROM Area a ORDER BY a.customer.firstName, a.customer.lastName")
    List<Object[]> findAreasWithCustomerDetails();
    
    /**
     * Find areas by customer with full address
     */
    @Query("SELECT a, CONCAT(a.areaName, ', ', a.city, ', ', a.state, ' - ', a.pincode) as fullAddress FROM Area a WHERE a.customer.customerId = :customerId")
    List<Object[]> findAreasByCustomerWithFullAddress(@Param("customerId") Integer customerId);
    
    /**
     * Find areas by city with full address
     */
    @Query("SELECT a, CONCAT(a.areaName, ', ', a.city, ', ', a.state, ' - ', a.pincode) as fullAddress FROM Area a WHERE LOWER(a.city) LIKE LOWER(CONCAT('%', :city, '%'))")
    List<Object[]> findAreasByCityWithFullAddress(@Param("city") String city);
    
    /**
     * Find areas by state with full address
     */
    @Query("SELECT a, CONCAT(a.areaName, ', ', a.city, ', ', a.state, ' - ', a.pincode) as fullAddress FROM Area a WHERE LOWER(a.state) LIKE LOWER(CONCAT('%', :state, '%'))")
    List<Object[]> findAreasByStateWithFullAddress(@Param("state") String state);
} 