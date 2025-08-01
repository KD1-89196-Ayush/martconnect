package com.sunbeam.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sunbeam.entities.OrderItem;

@Repository
public interface OrderItemDao extends JpaRepository<OrderItem, Integer> {
    
    /**
     * Find order items by order
     */
    List<OrderItem> findByOrder_OrderId(Integer orderId);
    
    /**
     * Find order items by product
     */
    List<OrderItem> findByProduct_ProductId(Integer productId);
    
    /**
     * Find order items by seller
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.seller.sellerId = :sellerId")
    List<OrderItem> findBySellerId(@Param("sellerId") Integer sellerId);
    
    /**
     * Find order items by customer
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.customer.customerId = :customerId")
    List<OrderItem> findByCustomerId(@Param("customerId") Integer customerId);
    
    /**
     * Find order items by quantity range
     */
    List<OrderItem> findByQuantityBetween(Integer minQuantity, Integer maxQuantity);
    
    /**
     * Find order items by price per unit range
     */
    List<OrderItem> findByPricePerUnitBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Find order items by quantity greater than specified value
     */
    List<OrderItem> findByQuantityGreaterThan(Integer minQuantity);
    
    /**
     * Find order items by price per unit greater than specified value
     */
    List<OrderItem> findByPricePerUnitGreaterThan(BigDecimal minPrice);
    
    /**
     * Find order items by order and product
     */
    List<OrderItem> findByOrder_OrderIdAndProduct_ProductId(Integer orderId, Integer productId);
    
    /**
     * Find order items by product and quantity range
     */
    List<OrderItem> findByProduct_ProductIdAndQuantityBetween(Integer productId, Integer minQuantity, Integer maxQuantity);
    
    /**
     * Find order items by product and price per unit range
     */
    List<OrderItem> findByProduct_ProductIdAndPricePerUnitBetween(Integer productId, BigDecimal minPrice, BigDecimal maxPrice);
    
    /**
     * Count order items by order
     */
    long countByOrder_OrderId(Integer orderId);
    
    /**
     * Count order items by product
     */
    long countByProduct_ProductId(Integer productId);
    
    /**
     * Count order items by seller
     */
    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.order.seller.sellerId = :sellerId")
    long countBySellerId(@Param("sellerId") Integer sellerId);
    
    /**
     * Count order items by customer
     */
    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.order.customer.customerId = :customerId")
    long countByCustomerId(@Param("customerId") Integer customerId);
    
    /**
     * Find order items created in a specific date range
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.createdAt BETWEEN :startDate AND :endDate")
    List<OrderItem> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                          @Param("endDate") java.time.LocalDateTime endDate);
    
    /**
     * Find order items with total value (quantity * price per unit)
     */
    @Query("SELECT oi, (oi.quantity * oi.pricePerUnit) as totalValue FROM OrderItem oi ORDER BY totalValue DESC")
    List<Object[]> findOrderItemsWithTotalValue();
    
    /**
     * Find order items by order with total value
     */
    @Query("SELECT oi, (oi.quantity * oi.pricePerUnit) as totalValue FROM OrderItem oi WHERE oi.order.orderId = :orderId ORDER BY totalValue DESC")
    List<Object[]> findOrderItemsByOrderWithTotalValue(@Param("orderId") Integer orderId);
    
    /**
     * Find order items by product with total value
     */
    @Query("SELECT oi, (oi.quantity * oi.pricePerUnit) as totalValue FROM OrderItem oi WHERE oi.product.productId = :productId ORDER BY totalValue DESC")
    List<Object[]> findOrderItemsByProductWithTotalValue(@Param("productId") Integer productId);
    
    /**
     * Find top selling products by quantity sold
     */
    @Query("SELECT oi.product, SUM(oi.quantity) as totalQuantity FROM OrderItem oi GROUP BY oi.product ORDER BY totalQuantity DESC")
    List<Object[]> findTopSellingProductsByQuantity();
    
    /**
     * Find top selling products by revenue
     */
    @Query("SELECT oi.product, SUM(oi.quantity * oi.pricePerUnit) as totalRevenue FROM OrderItem oi GROUP BY oi.product ORDER BY totalRevenue DESC")
    List<Object[]> findTopSellingProductsByRevenue();
    
    /**
     * Find order items by seller with total value
     */
    @Query("SELECT oi, (oi.quantity * oi.pricePerUnit) as totalValue FROM OrderItem oi WHERE oi.order.seller.sellerId = :sellerId ORDER BY totalValue DESC")
    List<Object[]> findOrderItemsBySellerWithTotalValue(@Param("sellerId") Integer sellerId);
    
    /**
     * Find order items by customer with total value
     */
    @Query("SELECT oi, (oi.quantity * oi.pricePerUnit) as totalValue FROM OrderItem oi WHERE oi.order.customer.customerId = :customerId ORDER BY totalValue DESC")
    List<Object[]> findOrderItemsByCustomerWithTotalValue(@Param("customerId") Integer customerId);
    
    /**
     * Find average price per unit by product
     */
    @Query("SELECT oi.product, AVG(oi.pricePerUnit) as avgPrice FROM OrderItem oi GROUP BY oi.product ORDER BY avgPrice DESC")
    List<Object[]> findAveragePriceByProduct();
    
    /**
     * Find total quantity sold by product
     */
    @Query("SELECT oi.product, SUM(oi.quantity) as totalQuantity FROM OrderItem oi GROUP BY oi.product ORDER BY totalQuantity DESC")
    List<Object[]> findTotalQuantityByProduct();
    
    /**
     * Find order items with high value (above threshold)
     */
    @Query("SELECT oi FROM OrderItem oi WHERE (oi.quantity * oi.pricePerUnit) > :threshold ORDER BY (oi.quantity * oi.pricePerUnit) DESC")
    List<OrderItem> findOrderItemsWithHighValue(@Param("threshold") BigDecimal threshold);
} 