package com.sunbeam.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sunbeam.dto.OrderDto;
import com.sunbeam.entities.Order.PaymentStatus;
import com.sunbeam.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        try {
            List<OrderDto> orders = orderService.findAll();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Integer id) {
        try {
            Optional<OrderDto> order = orderService.findById(id);
            if (order.isPresent()) {
                return ResponseEntity.ok(order.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("Received order request: " + request);
            
            // Extract order data from request
            Map<String, Object> customerMap = (Map<String, Object>) request.get("customer");
            Map<String, Object> sellerMap = (Map<String, Object>) request.get("seller");
            BigDecimal totalAmount = new BigDecimal(request.get("totalAmount").toString());
            String paymentStatus = (String) request.get("paymentStatus");
            String transactionId = (String) request.get("transactionId");
            List<Map<String, Object>> orderItemsList = (List<Map<String, Object>>) request.get("orderItems");
            
            System.out.println("Extracted data - Customer: " + customerMap + ", Seller: " + sellerMap + ", Total: " + totalAmount + ", Items: " + orderItemsList);
            
            if (customerMap == null || sellerMap == null) {
                return ResponseEntity.badRequest().build();
            }
            
            Integer customerId = (Integer) customerMap.get("customerId");
            Integer sellerId = (Integer) sellerMap.get("sellerId");
            
            // Try alternative field names if the primary ones are not found
            if (customerId == null) {
                customerId = (Integer) customerMap.get("customer_id");
            }
            if (sellerId == null) {
                sellerId = (Integer) sellerMap.get("seller_id");
            }
            
            if (customerId == null || sellerId == null) {
                return ResponseEntity.badRequest().build();
            }
            
            // Create order DTO
            OrderDto orderDto = new OrderDto();
            
            // Set customer and seller IDs
            orderDto.setCustomerId(customerId);
            orderDto.setSellerId(sellerId);
            
            System.out.println("Setting customer ID: " + customerId + ", seller ID: " + sellerId);
            
            // Set other fields
            System.out.println("Received totalAmount from frontend: " + totalAmount);
            
            // Calculate total from order items to verify
            BigDecimal calculatedTotal = BigDecimal.ZERO;
            if (orderItemsList != null) {
                for (Map<String, Object> itemMap : orderItemsList) {
                    Integer quantity = (Integer) itemMap.get("quantity");
                    BigDecimal pricePerUnit = new BigDecimal(itemMap.get("pricePerUnit").toString());
                    BigDecimal itemTotal = pricePerUnit.multiply(new BigDecimal(quantity));
                    calculatedTotal = calculatedTotal.add(itemTotal);
                    System.out.println("Item: qty=" + quantity + ", price=" + pricePerUnit + ", itemTotal=" + itemTotal);
                }
            }
            System.out.println("Calculated total from items: " + calculatedTotal);
            System.out.println("Frontend total: " + totalAmount);
            System.out.println("Difference: " + totalAmount.subtract(calculatedTotal));
            
            orderDto.setTotalAmount(totalAmount);
            orderDto.setDeliveryCharge(BigDecimal.ZERO);
            orderDto.setTransactionId(transactionId);
            
            // Set timestamps
            LocalDateTime now = LocalDateTime.now();
            orderDto.setCreatedAt(now);
            orderDto.setUpdatedAt(now);
            
            // Set payment status
            if (paymentStatus != null) {
                try {
                    orderDto.setPaymentStatus(PaymentStatus.valueOf(paymentStatus));
                } catch (IllegalArgumentException e) {
                    orderDto.setPaymentStatus(PaymentStatus.PAID);
                }
            } else {
                orderDto.setPaymentStatus(PaymentStatus.PAID);
            }
            
            // Create order items
            List<com.sunbeam.dto.OrderItemDto> orderItems = new ArrayList<>();
            if (orderItemsList != null) {
                for (Map<String, Object> itemMap : orderItemsList) {
                    Map<String, Object> productMap = (Map<String, Object>) itemMap.get("product");
                    Integer productId = (Integer) productMap.get("productId");
                    
                    // Try alternative field name if the primary one is not found
                    if (productId == null) {
                        productId = (Integer) productMap.get("product_id");
                    }
                    
                    Integer quantity = (Integer) itemMap.get("quantity");
                    BigDecimal pricePerUnit = new BigDecimal(itemMap.get("pricePerUnit").toString());
                    
                    if (productId == null || quantity == null || pricePerUnit == null) {
                        return ResponseEntity.badRequest().build();
                    }
                    
                    com.sunbeam.dto.OrderItemDto orderItemDto = new com.sunbeam.dto.OrderItemDto();
                    
                    // Set product ID
                    orderItemDto.setProductId(productId);
                    
                    orderItemDto.setQuantity(quantity);
                    orderItemDto.setPricePerUnit(pricePerUnit);
                    orderItemDto.setCreatedAt(now); // Set timestamp for order item
                    
                    orderItems.add(orderItemDto);
                    
                    System.out.println("Created order item for product ID: " + productId + ", quantity: " + quantity + ", price: " + pricePerUnit);
                }
            }
            
            orderDto.setOrderItems(orderItems);
            
            System.out.println("Creating order with service...");
            OrderDto createdOrder = orderService.createOrder(orderDto);
            System.out.println("Order created successfully with ID: " + createdOrder.getOrderId());
            System.out.println("Final order total: " + createdOrder.getTotalAmount());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
        } catch (Exception e) {
            System.err.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDto>> getCustomerOrders(@PathVariable Integer customerId) {
        try {
            System.out.println("Fetching orders for customer ID: " + customerId);
            
            // Return all customer orders with seller and order items to simplify frontend
            List<OrderDto> orders = orderService.findByCustomerWithSellerAndItems(customerId);
            
            System.out.println("Found " + orders.size() + " orders for customer " + customerId);
            if (orders.size() > 0) {
                System.out.println("First order: " + orders.get(0));
            }
            
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            System.err.println("Error fetching customer orders: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<OrderDto>> getSellerOrders(@PathVariable Integer sellerId) {
        try {
            // Return all seller orders with customer and order items to simplify frontend
            List<OrderDto> orders = orderService.findBySellerWithCustomerAndItems(sellerId);
            
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 