package com.sunbeam.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.sunbeam.entities.Customer;

/**
 * Service interface for Customer entity operations
 */
public interface CustomerService extends BaseService<Customer, Integer> {
    
    /**
     * Find customer by email
     * @param email The email to search for
     * @return Optional containing the customer if found
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Find customer by phone number
     * @param phone The phone number to search for
     * @return Optional containing the customer if found
     */
    Optional<Customer> findByPhone(String phone);
    
    /**
     * Find customers by first name
     * @param firstName The first name to search for
     * @return List of customers matching the first name
     */
    List<Customer> findByFirstName(String firstName);
    
    /**
     * Find customers by last name
     * @param lastName The last name to search for
     * @return List of customers matching the last name
     */
    List<Customer> findByLastName(String lastName);
    
    /**
     * Find customers by first name or last name containing search term
     * @param searchTerm The search term
     * @return List of customers matching the search term
     */
    List<Customer> findByFirstNameOrLastNameContaining(String searchTerm);
    
    /**
     * Check if customer exists by email
     * @param email The email to check
     * @return true if customer exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if customer exists by phone
     * @param phone The phone number to check
     * @return true if customer exists, false otherwise
     */
    boolean existsByPhone(String phone);
    
    /**
     * Find customers by city
     * @param city The city to search for
     * @return List of customers in the specified city
     */
    List<Customer> findByCity(String city);
    
    /**
     * Find customers by state
     * @param state The state to search for
     * @return List of customers in the specified state
     */
    List<Customer> findByState(String state);
    
    /**
     * Find customers by pincode
     * @param pincode The pincode to search for
     * @return List of customers with the specified pincode
     */
    List<Customer> findByPincode(String pincode);
    
    /**
     * Count total customers
     * @return The total number of customers
     */
    long countTotalCustomers();
    
    /**
     * Find customers created in a specific date range
     * @param startDate The start date
     * @param endDate The end date
     * @return List of customers created in the date range
     */
    List<Customer> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Register a new customer
     * @param customer The customer to register
     * @return The registered customer
     */
    Customer registerCustomer(Customer customer);
    
    /**
     * Update customer profile
     * @param customerId The customer ID
     * @param customer The updated customer data
     * @return The updated customer
     */
    Customer updateCustomerProfile(Integer customerId, Customer customer);
    
    /**
     * Change customer password
     * @param customerId The customer ID
     * @param oldPassword The old password
     * @param newPassword The new password
     * @return true if password changed successfully, false otherwise
     */
    boolean changePassword(Integer customerId, String oldPassword, String newPassword);
    
    /**
     * Authenticate customer
     * @param email The email
     * @param password The password
     * @return Optional containing the customer if authentication successful
     */
    Optional<Customer> authenticate(String email, String password);
    
    /**
     * Get customer statistics
     * @param customerId The customer ID
     * @return Customer statistics as Object array
     */
    Object[] getCustomerStatistics(Integer customerId);
    
    /**
     * Get top customers by order count
     * @param limit The maximum number of customers to return
     * @return List of top customers with order counts
     */
    List<Object[]> getTopCustomersByOrderCount(int limit);
    
    /**
     * Get top customers by total spent
     * @param limit The maximum number of customers to return
     * @return List of top customers with total spent amounts
     */
    List<Object[]> getTopCustomersByTotalSpent(int limit);
} 