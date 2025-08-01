package com.sunbeam.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sunbeam.entities.Order;
import com.sunbeam.entities.Order.PaymentStatus;

@Repository
public interface OrderDao extends JpaRepository<Order, Integer> {
    
    /**
     * Find orders by customer
     */
    List<Order> findByCustomer_CustomerId(Integer customerId);
    
    /**
     * Find orders by seller
     */
    List<Order> findBySeller_SellerId(Integer sellerId);
    
    /**
     * Find orders by payment status
     */
    List<Order> findByPaymentStatus(PaymentStatus paymentStatus);
    
    /**
     * Find orders by customer and payment status
     */
    List<Order> findByCustomer_CustomerIdAndPaymentStatus(Integer customerId, PaymentStatus paymentStatus);
    
    /**
     * Find orders by seller and payment status
     */
    List<Order> findBySeller_SellerIdAndPaymentStatus(Integer sellerId, PaymentStatus paymentStatus);
    
    /**
     * Find orders by total amount range
     */
    List<Order> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);
    
    /**
     * Find orders by order date range
     */
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    /**
     * Find orders by transaction ID
     */
    List<Order> findByTransactionIdContaining(String transactionId);
    
    /**
     * Find orders by customer with pagination
     */
    Page<Order> findByCustomer_CustomerId(Integer customerId, Pageable pageable);
    
    /**
     * Find orders by seller with pagination
     */
    Page<Order> findBySeller_SellerId(Integer sellerId, Pageable pageable);
    
    /**
     * Find orders by payment status with pagination
     */
    Page<Order> findByPaymentStatus(PaymentStatus paymentStatus, Pageable pageable);
    
    /**
     * Find orders by order date range with pagination
     */
    Page<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    /**
     * Count orders by customer
     */
    long countByCustomer_CustomerId(Integer customerId);
    
    /**
     * Count orders by seller
     */
    long countBySeller_SellerId(Integer sellerId);
    
    /**
     * Count orders by payment status
     */
    long countByPaymentStatus(PaymentStatus paymentStatus);
    
    /**
     * Count orders by customer and payment status
     */
    long countByCustomer_CustomerIdAndPaymentStatus(Integer customerId, PaymentStatus paymentStatus);
    
    /**
     * Count orders by seller and payment status
     */
    long countBySeller_SellerIdAndPaymentStatus(Integer sellerId, PaymentStatus paymentStatus);
    
    /**
     * Find orders created in a specific date range
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find orders updated in a specific date range
     */
    @Query("SELECT o FROM Order o WHERE o.updatedAt BETWEEN :startDate AND :endDate")
    List<Order> findByUpdatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);
    
    /**
     * Find orders with total amount greater than specified value
     */
    @Query("SELECT o FROM Order o WHERE o.totalAmount > :minAmount ORDER BY o.totalAmount DESC")
    List<Order> findOrdersWithHighValue(@Param("minAmount") BigDecimal minAmount);
    
    /**
     * Find orders with delivery charge greater than zero
     */
    @Query("SELECT o FROM Order o WHERE o.deliveryCharge > 0 ORDER BY o.deliveryCharge DESC")
    List<Order> findOrdersWithDeliveryCharge();
    
    /**
     * Find orders by customer email
     */
    @Query("SELECT o FROM Order o WHERE o.customer.email = :email")
    List<Order> findByCustomerEmail(@Param("email") String email);
    
    /**
     * Find orders by seller email
     */
    @Query("SELECT o FROM Order o WHERE o.seller.email = :email")
    List<Order> findBySellerEmail(@Param("email") String email);
    
    /**
     * Find orders by customer name (first name or last name)
     */
    @Query("SELECT o FROM Order o WHERE LOWER(o.customer.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(o.customer.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Order> findByCustomerName(@Param("name") String name);
    
    /**
     * Find orders by seller name (first name, last name, or shop name)
     */
    @Query("SELECT o FROM Order o WHERE LOWER(o.seller.firstName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(o.seller.lastName) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(o.seller.shopName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Order> findBySellerName(@Param("name") String name);
} 