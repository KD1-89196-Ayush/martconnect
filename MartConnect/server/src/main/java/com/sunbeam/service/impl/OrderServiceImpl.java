package com.sunbeam.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.OrderDao;
import com.sunbeam.dto.ApiResponse;
import com.sunbeam.dto.OrderDto;
import com.sunbeam.entities.Order;
import com.sunbeam.entities.Order.PaymentStatus;
import com.sunbeam.exception.ResourceNotFoundException;
import com.sunbeam.service.OrderService;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    
    private final OrderDao orderDao;
    private final ModelMapper modelMapper;
    
    // GET methods - return DTOs for frontend
    @Override
    public OrderDto getOrderById(Long orderId) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        return modelMapper.map(order, OrderDto.class);
    }
    
    @Override
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderDao.findAll();
        return orders.stream()
                .map(entity -> modelMapper.map(entity, OrderDto.class))
                .toList();
    }
    
    @Override
    public List<OrderDto> getCustomerOrders(Long customerId) {
        List<Order> orders = orderDao.findByCustomerId(customerId);
        return orders.stream()
                .map(entity -> modelMapper.map(entity, OrderDto.class))
                .toList();
    }
    
    @Override
    public List<OrderDto> getSellerOrders(Long sellerId) {
        List<Order> orders = orderDao.findBySellerId(sellerId);
        return orders.stream()
                .map(entity -> modelMapper.map(entity, OrderDto.class))
                .toList();
    }
    
    @Override
    public List<OrderDto> getOrdersByPaymentStatus(PaymentStatus paymentStatus) {
        List<Order> orders = orderDao.findByPaymentStatus(paymentStatus);
        return orders.stream()
                .map(entity -> modelMapper.map(entity, OrderDto.class))
                .toList();
    }
    
    @Override
    public List<OrderDto> getOrdersByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount) {
        List<Order> orders = orderDao.findByTotalAmountBetween(minAmount, maxAmount);
        return orders.stream()
                .map(entity -> modelMapper.map(entity, OrderDto.class))
                .toList();
    }
    
    @Override
    public List<OrderDto> getOrdersByTransactionIdContaining(String transactionId) {
        List<Order> orders = orderDao.findByTransactionIdContaining(transactionId);
        return orders.stream()
                .map(entity -> modelMapper.map(entity, OrderDto.class))
                .toList();
    }
    
    // Business logic methods
    @Override
    public long countByCustomer(Long customerId) {
        return orderDao.countByCustomerId(customerId);
    }
    
    @Override
    public long countBySeller(Long sellerId) {
        return orderDao.countBySellerId(sellerId);
    }
    
    @Override
    public long countByPaymentStatus(PaymentStatus paymentStatus) {
        return orderDao.countByPaymentStatus(paymentStatus);
    }
    
    // POST/PUT/DELETE methods - return ApiResponse
    public ApiResponse deleteOrder(Long orderId) {
        if (!orderDao.existsById(orderId)) {
            return new ApiResponse("Order not found with ID: " + orderId);
        }
        orderDao.deleteById(orderId);
        return new ApiResponse("Order deleted successfully");
    }
    
    public ApiResponse createOrder(OrderDto orderDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        Order savedOrder = orderDao.save(order);
        return new ApiResponse("Order created successfully with ID " + savedOrder.getId());
    }
    
    public ApiResponse updateOrder(Long orderId, OrderDto orderDto) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + orderId));
        modelMapper.map(orderDto, order);
        orderDao.save(order);
        return new ApiResponse("Order updated successfully");
    }
    
    // Basic CRUD operations from BaseService
    @Override
    public Order save(Order entity) {
        return orderDao.save(entity);
    }
    
    @Override
    public Optional<Order> findById(Long id) {
        return orderDao.findById(id);
    }
    
    @Override
    public List<Order> findAll() {
        return orderDao.findAll();
    }
    
    @Override
    public void deleteById(Long id) {
        orderDao.deleteById(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return orderDao.existsById(id);
    }
    
    @Override
    public long count() {
        return orderDao.count();
    }
} 