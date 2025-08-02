package com.sunbeam.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sunbeam.dto.ApiResponse;
import com.sunbeam.entities.Customer;
import com.sunbeam.entities.Order;
import com.sunbeam.entities.Order.PaymentStatus;
import com.sunbeam.entities.OrderItem;
import com.sunbeam.entities.Product;
import com.sunbeam.entities.Seller;
import com.sunbeam.service.OrderService;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping
    public ResponseEntity<ApiResponse> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Order> orders = orderService.findAll();
            
            return ResponseEntity.ok(ApiResponse.success("Orders retrieved successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve orders: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Integer id) {
        try {
            Optional<Order> order = orderService.findById(id);
            if (order.isPresent()) {
                return ResponseEntity.ok(ApiResponse.success("Order retrieved successfully", order.get()));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Order not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve order: " + e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("Received order request: " + request);
            
            // Extract order data from request
            Map<String, Object> customerMap = (Map<String, Object>) request.get("customer");
            Map<String, Object> sellerMap = (Map<String, Object>) request.get("seller");
            BigDecimal totalAmount = new BigDecimal(request.get("totalAmount").toString());
            BigDecimal deliveryCharge = request.get("deliveryCharge") != null ? 
                new BigDecimal(request.get("deliveryCharge").toString()) : BigDecimal.ZERO;
            String paymentStatus = (String) request.get("paymentStatus");
            String transactionId = (String) request.get("transactionId");
            List<Map<String, Object>> orderItemsList = (List<Map<String, Object>>) request.get("orderItems");
            
            System.out.println("Extracted data - Customer: " + customerMap + ", Seller: " + sellerMap + ", Total: " + totalAmount + ", Items: " + orderItemsList);
            
            if (customerMap == null || sellerMap == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Customer and seller information are required"));
            }
            
            Integer customerId = (Integer) customerMap.get("customerId");
            Integer sellerId = (Integer) sellerMap.get("sellerId");
            
            if (customerId == null || sellerId == null) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("Customer ID and seller ID are required"));
            }
            
                         // Create order entity
             Order order = new Order();
             
             // Set customer
             Customer customer = new Customer();
             customer.setCustomerId(customerId);
             order.setCustomer(customer);
             
             // Set seller
             Seller seller = new Seller();
             seller.setSellerId(sellerId);
             order.setSeller(seller);
             
             System.out.println("Setting customer ID: " + customerId + ", seller ID: " + sellerId);
            
            // Set other fields
            order.setTotalAmount(totalAmount);
            order.setDeliveryCharge(deliveryCharge);
            order.setTransactionId(transactionId);
            
            // Set payment status
            if (paymentStatus != null) {
                try {
                    order.setPaymentStatus(Order.PaymentStatus.valueOf(paymentStatus));
                } catch (IllegalArgumentException e) {
                    order.setPaymentStatus(Order.PaymentStatus.PENDING);
                }
            }
            
            // Create order items
            List<OrderItem> orderItems = new ArrayList<>();
            if (orderItemsList != null) {
                for (Map<String, Object> itemMap : orderItemsList) {
                    Map<String, Object> productMap = (Map<String, Object>) itemMap.get("product");
                    Integer productId = (Integer) productMap.get("productId");
                    Integer quantity = (Integer) itemMap.get("quantity");
                    BigDecimal pricePerUnit = new BigDecimal(itemMap.get("pricePerUnit").toString());
                    
                    if (productId == null || quantity == null || pricePerUnit == null) {
                        return ResponseEntity.badRequest()
                                .body(ApiResponse.error("Product ID, quantity, and price are required for order items"));
                    }
                    
                    OrderItem orderItem = new OrderItem();
                    
                                         // Set product
                     Product product = new Product();
                     product.setProductId(productId);
                     orderItem.setProduct(product);
                     
                     orderItem.setQuantity(quantity);
                     orderItem.setPricePerUnit(pricePerUnit);
                     orderItem.setOrder(order);
                     
                     orderItems.add(orderItem);
                     
                     System.out.println("Created order item for product ID: " + productId + ", quantity: " + quantity + ", price: " + pricePerUnit);
                }
            }
            
            order.setOrderItems(orderItems);
            
            System.out.println("Creating order with service...");
            Order createdOrder = orderService.createOrder(order);
            System.out.println("Order created successfully with ID: " + createdOrder.getOrderId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("Order created successfully", createdOrder));
        } catch (Exception e) {
            System.err.println("Error creating order: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create order: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/payment-status")
    public ResponseEntity<ApiResponse> updatePaymentStatus(
            @PathVariable Integer id,
            @RequestParam PaymentStatus paymentStatus) {
        try {
            Order updatedOrder = orderService.updatePaymentStatus(id, paymentStatus);
            return ResponseEntity.ok(ApiResponse.success("Payment status updated successfully", updatedOrder));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update payment status: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Integer id) {
        try {
            Order cancelledOrder = orderService.cancelOrder(id);
            return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", cancelledOrder));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to cancel order: " + e.getMessage()));
        }
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ApiResponse> getCustomerOrders(
            @PathVariable Integer customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // For now, return all customer orders without pagination to simplify frontend
            List<Order> orders = orderService.findByCustomer(customerId);
            
            return ResponseEntity.ok(ApiResponse.success("Customer orders retrieved successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve customer orders: " + e.getMessage()));
        }
    }
    
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<ApiResponse> getSellerOrders(
            @PathVariable Integer sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Order> orders = orderService.findBySeller(sellerId, pageable);
            
            return ResponseEntity.ok(ApiResponse.success("Seller orders retrieved successfully", orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve seller orders: " + e.getMessage()));
        }
    }
    
    @GetMapping("/status/{paymentStatus}")
    public ResponseEntity<ApiResponse> getOrdersByPaymentStatus(
            @PathVariable PaymentStatus paymentStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Order> orders = orderService.findByPaymentStatus(paymentStatus, pageable);
            
            return ResponseEntity.ok(ApiResponse.success("Orders by payment status retrieved", orders));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to retrieve orders by payment status: " + e.getMessage()));
        }
    }
} 