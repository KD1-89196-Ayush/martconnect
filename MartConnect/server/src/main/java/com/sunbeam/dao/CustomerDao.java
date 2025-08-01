package com.sunbeam.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sunbeam.entities.Customer;

@Repository
public interface CustomerDao extends JpaRepository<Customer, Integer> {
    
    /**
     * Find customer by email
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Find customer by phone number
     */
    Optional<Customer> findByPhone(String phone);
    
    /**
     * Find customers by first name
     */
    List<Customer> findByFirstNameContainingIgnoreCase(String firstName);
    
    /**
     * Find customers by last name
     */
    List<Customer> findByLastNameContainingIgnoreCase(String lastName);
    
    /**
     * Find customers by first name or last name containing the search term
     */
    @Query("SELECT c FROM Customer c WHERE LOWER(c.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Customer> findByFirstNameOrLastNameContaining(@Param("searchTerm") String searchTerm);
    
    /**
     * Check if customer exists by email
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if customer exists by phone
     */
    boolean existsByPhone(String phone);
    
    /**
     * Find customers by city (from their areas)
     */
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.areas a WHERE LOWER(a.city) LIKE LOWER(CONCAT('%', :city, '%'))")
    List<Customer> findByCity(@Param("city") String city);
    
    /**
     * Find customers by state (from their areas)
     */
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.areas a WHERE LOWER(a.state) LIKE LOWER(CONCAT('%', :state, '%'))")
    List<Customer> findByState(@Param("state") String state);
    
    /**
     * Find customers by pincode (from their areas)
     */
    @Query("SELECT DISTINCT c FROM Customer c JOIN c.areas a WHERE a.pincode = :pincode")
    List<Customer> findByPincode(@Param("pincode") String pincode);
    
    /**
     * Count total customers
     */
    @Query("SELECT COUNT(c) FROM Customer c")
    long countTotalCustomers();
    
    /**
     * Find customers created in a specific date range
     */
    @Query("SELECT c FROM Customer c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    List<Customer> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                         @Param("endDate") java.time.LocalDateTime endDate);
} 