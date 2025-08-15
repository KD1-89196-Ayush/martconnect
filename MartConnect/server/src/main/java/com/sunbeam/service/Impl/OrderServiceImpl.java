package com.sunbeam.service.Impl;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sunbeam.dao.CustomerDao;
import com.sunbeam.dao.OrderDao;
import com.sunbeam.dao.ProductDao;
import com.sunbeam.dao.SellerDao;
import com.sunbeam.dto.CustomerDto;
import com.sunbeam.dto.OrderDto;
import com.sunbeam.dto.ProductDto;
import com.sunbeam.dto.SellerDto;
import com.sunbeam.entities.Customer;
import com.sunbeam.entities.Order;
import com.sunbeam.entities.OrderItem;
import com.sunbeam.entities.Product;
import com.sunbeam.entities.Seller;
import com.sunbeam.service.OrderService;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    
    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private ProductDao productDao;
    
    @Autowired
    private CustomerDao customerDao;
    
    @Autowired
    private SellerDao sellerDao;
    
    @Autowired
    private ModelMapper modelMapper;
    
    // Core: Create order with validation and stock update
    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        
        // Handle customer by ID
        if (orderDto.getCustomerId() != null) {
            Optional<Customer> customerOpt = customerDao.findById(orderDto.getCustomerId());
            if (customerOpt.isPresent()) {
                order.setCustomer(customerOpt.get());
            } else {
                throw new RuntimeException("Customer not found with ID: " + orderDto.getCustomerId());
            }
        } else {
            throw new RuntimeException("Customer ID is required for order");
        }
        
        // Handle seller by ID
        if (orderDto.getSellerId() != null) {
            Optional<Seller> sellerOpt = sellerDao.findById(orderDto.getSellerId());
            if (sellerOpt.isPresent()) {
                order.setSeller(sellerOpt.get());
            } else {
                throw new RuntimeException("Seller not found with ID: " + orderDto.getSellerId());
            }
        } else {
            throw new RuntimeException("Seller ID is required for order");
        }
        
        // Handle order items
        if (orderDto.getOrderItems() != null && !orderDto.getOrderItems().isEmpty()) {
            List<OrderItem> orderItems = new ArrayList<>();
            for (com.sunbeam.dto.OrderItemDto itemDto : orderDto.getOrderItems()) {
                OrderItem item = modelMapper.map(itemDto, OrderItem.class);
                
                // Set the order reference
                item.setOrder(order);
                
                // Handle product by ID
                if (itemDto.getProductId() != null) {
                    Optional<Product> productOpt = productDao.findById(itemDto.getProductId());
                    if (productOpt.isPresent()) {
                        item.setProduct(productOpt.get());
                    } else {
                        throw new RuntimeException("Product not found with ID: " + itemDto.getProductId());
                    }
                } else {
                    throw new RuntimeException("Product ID is required for order item");
                }
                
                orderItems.add(item);
            }
            order.setOrderItems(orderItems);
        }
        
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentStatus(Order.PaymentStatus.PAID);
        
        // Store order items before saving order
        List<OrderItem> orderItems = order.getOrderItems();
        
        // Save the order first (this will cascade save the order items)
        Order savedOrder = orderDao.save(order);
        
        // Update stock for each order item
        if (orderItems != null && !orderItems.isEmpty()) {
            for (OrderItem item : orderItems) {
                Optional<Product> productOpt = productDao.findById(item.getProduct().getProductId());
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();
                    int newStock = product.getStock() - item.getQuantity();
                    if (newStock < 0) {
                        throw new RuntimeException("Insufficient stock for product: " + product.getName());
                    }
                    product.setStock(newStock);
                    product.setUpdatedAt(LocalDateTime.now());
                    productDao.save(product);
                }
            }
        }
        
        OrderDto resultDto = modelMapper.map(savedOrder, OrderDto.class);
        
        // Populate customer and seller objects for frontend compatibility
        if (savedOrder.getCustomer() != null) {
            resultDto.setCustomerName(savedOrder.getCustomer().getFirstName() + " " + savedOrder.getCustomer().getLastName());
            CustomerDto customerDto = modelMapper.map(savedOrder.getCustomer(), CustomerDto.class);
            resultDto.setCustomer(customerDto);
        }
        
        if (savedOrder.getSeller() != null) {
            resultDto.setSellerName(savedOrder.getSeller().getFirstName() + " " + savedOrder.getSeller().getLastName());
            resultDto.setShopName(savedOrder.getSeller().getShopName());
            SellerDto sellerDto = modelMapper.map(savedOrder.getSeller(), SellerDto.class);
            resultDto.setSeller(sellerDto);
        }
        
        // Populate order items with product information
        if (savedOrder.getOrderItems() != null && !savedOrder.getOrderItems().isEmpty()) {
            List<com.sunbeam.dto.OrderItemDto> orderItemDtos = savedOrder.getOrderItems().stream()
                    .map(item -> {
                        com.sunbeam.dto.OrderItemDto itemDto = modelMapper.map(item, com.sunbeam.dto.OrderItemDto.class);
                        
                        // Populate product information
                        if (item.getProduct() != null) {
                            itemDto.setProductName(item.getProduct().getName());
                            itemDto.setProductImageUrl(item.getProduct().getImageUrl());
                            
                            // Create product object for frontend compatibility
                            ProductDto productDto = modelMapper.map(item.getProduct(), ProductDto.class);
                            itemDto.setProduct(productDto);
                        }
                        
                        // Calculate total price
                        if (item.getPricePerUnit() != null && item.getQuantity() != null) {
                            itemDto.setTotalPrice(item.getPricePerUnit().multiply(new BigDecimal(item.getQuantity())));
                        }
                        
                        return itemDto;
                    })
                    .collect(Collectors.toList());
            resultDto.setOrderItems(orderItemDtos);
        }
        
        return resultDto;
    }
    

//    // Core: Cancel order and restore stock
//    @Override
//    public OrderDto cancelOrder(Integer orderId) {
//        Optional<Order> orderOpt = orderDao.findById(orderId);
//        if (orderOpt.isEmpty()) {
//            throw new RuntimeException("Order not found with ID: " + orderId);
//        }
//        
//        Order order = orderOpt.get();
//        
//        // Restore stock for each order item
//        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
//            for (OrderItem item : order.getOrderItems()) {
//                Optional<Product> productOpt = productDao.findById(item.getProduct().getProductId());
//                if (productOpt.isPresent()) {
//                    Product product = productOpt.get();
//                    int newStock = product.getStock() + item.getQuantity();
//                    product.setStock(newStock);
//                    product.setUpdatedAt(LocalDateTime.now());
//                    productDao.save(product);
//                }
//            }
//        }
//        
//        order.setPaymentStatus(Order.PaymentStatus.CANCELLED);
//        order.setUpdatedAt(LocalDateTime.now());
//        Order savedOrder = orderDao.save(order);
//        return modelMapper.map(savedOrder, OrderDto.class);
//    }

    
    // Essential CRUD methods
    @Override
    public OrderDto save(OrderDto orderDto) { 
        Order order = modelMapper.map(orderDto, Order.class);
        
        // Handle customer by ID
        if (orderDto.getCustomerId() != null) {
            Optional<Customer> customerOpt = customerDao.findById(orderDto.getCustomerId());
            if (customerOpt.isPresent()) {
                order.setCustomer(customerOpt.get());
            } else {
                throw new RuntimeException("Customer not found with ID: " + orderDto.getCustomerId());
            }
        }
        
        // Handle seller by ID
        if (orderDto.getSellerId() != null) {
            Optional<Seller> sellerOpt = sellerDao.findById(orderDto.getSellerId());
            if (sellerOpt.isPresent()) {
                order.setSeller(sellerOpt.get());
            } else {
                throw new RuntimeException("Seller not found with ID: " + orderDto.getSellerId());
            }
        }
        
        Order savedOrder = orderDao.save(order);
        return modelMapper.map(savedOrder, OrderDto.class);
    }
    
    @Override
    public Optional<OrderDto> findById(Integer id) { 
        Optional<Order> orderOpt = orderDao.findById(id);
        return orderOpt.map(order -> {
            OrderDto orderDto = modelMapper.map(order, OrderDto.class);
            
            // Populate customer and seller objects for frontend compatibility
            if (order.getCustomer() != null) {
                orderDto.setCustomerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
                CustomerDto customerDto = modelMapper.map(order.getCustomer(), CustomerDto.class);
                orderDto.setCustomer(customerDto);
            }
            
            if (order.getSeller() != null) {
                orderDto.setSellerName(order.getSeller().getFirstName() + " " + order.getSeller().getLastName());
                orderDto.setShopName(order.getSeller().getShopName());
                SellerDto sellerDto = modelMapper.map(order.getSeller(), SellerDto.class);
                orderDto.setSeller(sellerDto);
            }
            
            // Populate order items with product information
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                List<com.sunbeam.dto.OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                        .map(item -> {
                            com.sunbeam.dto.OrderItemDto itemDto = modelMapper.map(item, com.sunbeam.dto.OrderItemDto.class);
                            
                            // Populate product information
                            if (item.getProduct() != null) {
                                itemDto.setProductName(item.getProduct().getName());
                                itemDto.setProductImageUrl(item.getProduct().getImageUrl());
                                
                                // Create product object for frontend compatibility
                                ProductDto productDto = modelMapper.map(item.getProduct(), ProductDto.class);
                                itemDto.setProduct(productDto);
                            }
                            
                            // Calculate total price
                            if (item.getPricePerUnit() != null && item.getQuantity() != null) {
                                itemDto.setTotalPrice(item.getPricePerUnit().multiply(new BigDecimal(item.getQuantity())));
                            }
                            
                            return itemDto;
                        })
                        .collect(Collectors.toList());
                orderDto.setOrderItems(orderItemDtos);
            }
            
            return orderDto;
        });
    }
    
    @Override
    public List<OrderDto> findAll() { 
        return orderDao.findAll().stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Integer id) { orderDao.deleteById(id); }
    
    @Override
    public boolean existsById(Integer id) { return orderDao.existsById(id); }
    
    @Override
    public long count() { return orderDao.count(); }
    
    @Override
    public List<OrderDto> findBySellerWithCustomerAndItems(Integer sellerId) { 
        return orderDao.findBySellerWithCustomerAndItems(sellerId).stream()
                .map(order -> {
                    OrderDto orderDto = modelMapper.map(order, OrderDto.class);
                    
                    // Manually populate customer information
                    if (order.getCustomer() != null) {
                        orderDto.setCustomerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
                        
                        // Create customer object for frontend compatibility
                        CustomerDto customerDto = modelMapper.map(order.getCustomer(), CustomerDto.class);
                        orderDto.setCustomer(customerDto);
                    }
                    
                    // Manually populate seller information
                    if (order.getSeller() != null) {
                        orderDto.setSellerName(order.getSeller().getFirstName() + " " + order.getSeller().getLastName());
                        orderDto.setShopName(order.getSeller().getShopName());
                        
                        // Create seller object for frontend compatibility
                        SellerDto sellerDto = modelMapper.map(order.getSeller(), SellerDto.class);
                        orderDto.setSeller(sellerDto);
                    }
                    
                    // Manually populate order items with product information
                    if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                        List<com.sunbeam.dto.OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                                .map(item -> {
                                    com.sunbeam.dto.OrderItemDto itemDto = modelMapper.map(item, com.sunbeam.dto.OrderItemDto.class);
                                    
                                    // Populate product information
                                    if (item.getProduct() != null) {
                                        itemDto.setProductName(item.getProduct().getName());
                                        itemDto.setProductImageUrl(item.getProduct().getImageUrl());
                                        
                                        // Create product object for frontend compatibility
                                        ProductDto productDto = modelMapper.map(item.getProduct(), ProductDto.class);
                                        itemDto.setProduct(productDto);
                                    }
                                    
                                    // Calculate total price
                                    if (item.getPricePerUnit() != null && item.getQuantity() != null) {
                                        itemDto.setTotalPrice(item.getPricePerUnit().multiply(new BigDecimal(item.getQuantity())));
                                    }
                                    
                                    return itemDto;
                                })
                                .collect(Collectors.toList());
                        orderDto.setOrderItems(orderItemDtos);
                    }
                    
                    return orderDto;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public List<OrderDto> findByCustomerWithSellerAndItems(Integer customerId) { 
        return orderDao.findByCustomerWithSellerAndItems(customerId).stream()
                .map(order -> {
                    OrderDto orderDto = modelMapper.map(order, OrderDto.class);
                    
                    // Manually populate customer information
                    if (order.getCustomer() != null) {
                        orderDto.setCustomerName(order.getCustomer().getFirstName() + " " + order.getCustomer().getLastName());
                        
                        // Create customer object for frontend compatibility
                        CustomerDto customerDto = modelMapper.map(order.getCustomer(), CustomerDto.class);
                        orderDto.setCustomer(customerDto);
                    }
                    
                    // Manually populate seller information
                    if (order.getSeller() != null) {
                        orderDto.setSellerName(order.getSeller().getFirstName() + " " + order.getSeller().getLastName());
                        orderDto.setShopName(order.getSeller().getShopName());
                        
                        // Create seller object for frontend compatibility
                        SellerDto sellerDto = modelMapper.map(order.getSeller(), SellerDto.class);
                        orderDto.setSeller(sellerDto);
                    }
                    
                    // Manually populate order items with product information
                    if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                        List<com.sunbeam.dto.OrderItemDto> orderItemDtos = order.getOrderItems().stream()
                                .map(item -> {
                                    com.sunbeam.dto.OrderItemDto itemDto = modelMapper.map(item, com.sunbeam.dto.OrderItemDto.class);
                                    
                                    // Populate product information
                                    if (item.getProduct() != null) {
                                        itemDto.setProductName(item.getProduct().getName());
                                        itemDto.setProductImageUrl(item.getProduct().getImageUrl());
                                        
                                        // Create product object for frontend compatibility
                                        ProductDto productDto = modelMapper.map(item.getProduct(), ProductDto.class);
                                        itemDto.setProduct(productDto);
                                    }
                                    
                                    // Calculate total price
                                    if (item.getPricePerUnit() != null && item.getQuantity() != null) {
                                        itemDto.setTotalPrice(item.getPricePerUnit().multiply(new BigDecimal(item.getQuantity())));
                                    }
                                    
                                    return itemDto;
                                })
                                .collect(Collectors.toList());
                        orderDto.setOrderItems(orderItemDtos);
                    }
                    
                    return orderDto;
                })
                .collect(Collectors.toList());
    }
}