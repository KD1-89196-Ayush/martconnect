-- Backend Sample Data for MartConnect
-- This file contains different sample data to differentiate from frontend JSON data
-- Use this when connecting to backend database

USE martconnect;

-- Clear existing data (optional - uncomment if needed)
-- DELETE FROM order_items;
-- DELETE FROM orders;
-- DELETE FROM cart;
-- DELETE FROM areas;
-- DELETE FROM products;
-- DELETE FROM customers;
-- DELETE FROM sellers;

-- Insert different sample sellers for backend (focusing on grocery)
INSERT INTO sellers (first_name, last_name, email, phone, password, shop_name, shop_address) VALUES
('Rajesh', 'Patel', 'rajesh@freshmart.com', '9876543211', 'rajesh@2024', 'FreshMart Grocery', 'Andheri West, Mumbai, Maharashtra'),
('Priyanka', 'Shah', 'priyanka@organicstore.com', '9123456781', 'organic@2024', 'Organic Food Store', 'Koregaon Park, Pune, Maharashtra'),
('Amit', 'Kumar', 'amit@dailyneeds.com', '9988776656', 'daily@2024', 'Daily Needs Supermarket', 'Viman Nagar, Pune, Maharashtra'),
('Sneha', 'Verma', 'sneha@healthfood.com', '9090909091', 'health@2024', 'Health Food Corner', 'Bandra West, Mumbai, Maharashtra'),
('Vikrant', 'Singh', 'vikrant@groceryhub.com', '9876543212', 'grocery@2024', 'Grocery Hub', 'Kharghar, Navi Mumbai, Maharashtra');

-- Insert different sample customers for backend
INSERT INTO customers (first_name, last_name, email, phone, address, password) VALUES
('Arjun', 'Reddy', 'arjun.reddy@email.com', '9876501235', 'Baner, Pune', 'arjun@2024'),
('Kavya', 'Iyer', 'kavya.iyer@email.com', '9988776657', 'Worli, Mumbai', 'kavya@2024'),
('Rohan', 'Malhotra', 'rohan.malhotra@email.com', '9090909092', 'Hinjewadi, Pune', 'rohan@2024'),
('Zara', 'Khan', 'zara.khan@email.com', '9123456782', 'Powai, Mumbai', 'zara@2024'),
('Aditya', 'Joshi', 'aditya.joshi@email.com', '9876543213', 'Kalyani Nagar, Pune', 'aditya@2024'),
('Ishita', 'Gupta', 'ishita.gupta@email.com', '9988776658', 'Juhu, Mumbai', 'ishita@2024');

-- Insert grocery products for backend (focusing on grocery categories)
INSERT INTO products (name, price, unit, stock, image_url, description, category, seller_id) VALUES
('Fresh Tomatoes', 45.00, 'kg', 100, '/backend_images/tomatoes.jpg', 'Fresh red tomatoes, farm to table.', 'Fresh Vegetables', 1),
('Onions', 35.50, 'kg', 150, '/backend_images/onions.jpg', 'Fresh white onions, perfect for cooking.', 'Fresh Vegetables', 1),
('Potatoes', 40.00, 'kg', 200, '/backend_images/potatoes.jpg', 'Fresh potatoes, great for all dishes.', 'Fresh Vegetables', 1),
('Carrots', 60.00, 'kg', 80, '/backend_images/carrots.jpg', 'Fresh orange carrots, rich in vitamins.', 'Fresh Vegetables', 1),
('Fresh Milk', 65.00, 'liter', 50, '/backend_images/milk.jpg', 'Pure cow milk, delivered daily.', 'Dairy Products', 2),
('Paneer', 180.00, 'kg', 30, '/backend_images/paneer.jpg', 'Fresh homemade paneer.', 'Dairy Products', 2),
('Curd', 45.00, 'kg', 40, '/backend_images/curd.jpg', 'Fresh homemade curd.', 'Dairy Products', 2),
('Butter', 120.00, 'pack', 25, '/backend_images/butter.jpg', 'Pure butter, 100g pack.', 'Dairy Products', 2),
('Basmati Rice', 85.00, 'kg', 100, '/backend_images/basmati_rice.jpg', 'Premium quality basmati rice.', 'Grains', 3),
('Toor Dal', 120.00, 'kg', 80, '/backend_images/toor_dal.jpg', 'Organic toor dal.', 'Grains', 3),
('Wheat Flour', 45.00, 'kg', 120, '/backend_images/wheat_flour.jpg', 'Fresh wheat flour.', 'Grains', 3),
('Chana Dal', 95.00, 'kg', 90, '/backend_images/chana_dal.jpg', 'Organic chana dal.', 'Grains', 3),
('Sunflower Oil', 140.00, 'liter', 60, '/backend_images/sunflower_oil.jpg', 'Pure sunflower oil.', 'Edible Oil', 4),
('Mustard Oil', 160.00, 'liter', 40, '/backend_images/mustard_oil.jpg', 'Pure mustard oil.', 'Edible Oil', 4),
('Olive Oil', 450.00, 'liter', 20, '/backend_images/olive_oil.jpg', 'Extra virgin olive oil.', 'Edible Oil', 4),
('Coconut Oil', 180.00, 'liter', 35, '/backend_images/coconut_oil.jpg', 'Pure coconut oil.', 'Edible Oil', 4),
('Bathing Soap', 25.00, 'piece', 200, '/backend_images/bathing_soap.jpg', 'Natural bathing soap.', 'Soaps', 5),
('Washing Powder', 85.00, 'kg', 80, '/backend_images/washing_powder.jpg', 'Eco-friendly washing powder.', 'Soaps', 5),
('Dish Soap', 45.00, 'bottle', 100, '/backend_images/dish_soap.jpg', 'Liquid dish soap.', 'Soaps', 5),
('Hand Wash', 35.00, 'bottle', 120, '/backend_images/hand_wash.jpg', 'Antibacterial hand wash.', 'Soaps', 5);

-- Insert different sample areas for backend
INSERT INTO areas (pincode, area_name, city, state, customer_id) VALUES
('411045', 'Baner', 'Pune', 'Maharashtra', 1),
('400018', 'Worli', 'Mumbai', 'Maharashtra', 2),
('411057', 'Hinjewadi', 'Pune', 'Maharashtra', 3),
('400076', 'Powai', 'Mumbai', 'Maharashtra', 4),
('411006', 'Kalyani Nagar', 'Pune', 'Maharashtra', 5),
('400049', 'Juhu', 'Mumbai', 'Maharashtra', 6);

-- Insert sample orders for backend
INSERT INTO orders (customer_id, seller_id, total_amount, delivery_charge, payment_status, order_date, transaction_id) VALUES
(1, 1, 185.50, 30.00, 'Paid', '2024-01-15 10:30:00', 'TXN001'),
(2, 2, 290.00, 25.00, 'Paid', '2024-01-16 14:20:00', 'TXN002'),
(3, 3, 345.00, 35.00, 'Pending', '2024-01-17 09:15:00', 'TXN003'),
(4, 4, 830.00, 40.00, 'Paid', '2024-01-18 16:45:00', 'TXN004'),
(5, 5, 190.00, 30.00, 'Paid', '2024-01-19 11:30:00', 'TXN005'),
(6, 1, 140.50, 25.00, 'Pending', '2024-01-20 13:20:00', 'TXN006'),
(1, 3, 205.00, 30.00, 'Paid', '2024-01-21 08:45:00', 'TXN007'),
(2, 4, 450.00, 35.00, 'Paid', '2024-01-22 15:10:00', 'TXN008');

-- Insert sample order items for backend
INSERT INTO order_items (order_id, product_id, quantity, price_per_unit) VALUES
-- Order 1: Customer 1 buys from Seller 1 (Fresh Vegetables)
(1, 1, 2, 45.00),  -- Fresh Tomatoes (2kg)
(1, 2, 1, 35.50),  -- Onions (1kg)
(1, 3, 1, 40.00),  -- Potatoes (1kg)

-- Order 2: Customer 2 buys from Seller 2 (Dairy Products)
(2, 5, 2, 65.00),  -- Fresh Milk (2 liters)
(2, 6, 0.5, 180.00), -- Paneer (0.5kg)
(2, 7, 1, 45.00),  -- Curd (1kg)

-- Order 3: Customer 3 buys from Seller 3 (Grains & Pulses)
(3, 9, 2, 85.00),  -- Basmati Rice (2kg)
(3, 10, 1, 120.00), -- Toor Dal (1kg)
(3, 11, 1, 45.00), -- Wheat Flour (1kg)

-- Order 4: Customer 4 buys from Seller 4 (Edible Oils)
(4, 13, 2, 140.00), -- Sunflower Oil (2 liters)
(4, 14, 1, 160.00), -- Mustard Oil (1 liter)
(4, 15, 1, 450.00), -- Olive Oil (1 liter)

-- Order 5: Customer 5 buys from Seller 5 (Soaps & Detergents)
(5, 17, 2, 25.00), -- Bathing Soap (2 pieces)
(5, 18, 1, 85.00), -- Washing Powder (1kg)
(5, 19, 1, 45.00), -- Dish Soap (1 bottle)

-- Order 6: Customer 6 buys from Seller 1 (Fresh Vegetables)
(6, 1, 1, 45.00),  -- Fresh Tomatoes (1kg)
(6, 4, 1, 60.00),  -- Carrots (1kg)

-- Order 7: Customer 1 buys from Seller 3 (Grains & Pulses)
(7, 12, 1, 95.00), -- Chana Dal (1kg)
(7, 11, 1, 45.00), -- Wheat Flour (1kg)
(7, 9, 1, 85.00),  -- Basmati Rice (1kg)

-- Order 8: Customer 2 buys from Seller 4 (Edible Oils)
(8, 15, 1, 450.00); -- Olive Oil (1 liter)

-- Insert sample cart items for backend
INSERT INTO cart (customer_id, product_id, quantity) VALUES
(1, 1, 2),   -- Customer 1: 2kg Fresh Tomatoes
(1, 5, 1),   -- Customer 1: 1 liter Fresh Milk
(2, 6, 1),   -- Customer 2: 1kg Paneer
(3, 9, 3),   -- Customer 3: 3kg Basmati Rice
(4, 13, 2),  -- Customer 4: 2 liters Sunflower Oil
(5, 17, 5),  -- Customer 5: 5 Bathing Soaps
(6, 2, 2);   -- Customer 6: 2kg Onions

-- Insert sample user sessions for backend
INSERT INTO user_sessions (session_id, user_id, user_type, token, expires_at) VALUES
('sess_001', 1, 'customer', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.backend_customer_1', '2024-02-15 10:30:00'),
('sess_002', 2, 'customer', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.backend_customer_2', '2024-02-15 14:20:00'),
('sess_003', 1, 'seller', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.backend_seller_1', '2024-02-15 09:15:00'),
('sess_004', 2, 'seller', 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.backend_seller_2', '2024-02-15 16:45:00');

-- Insert sample product images for backend
INSERT INTO product_images (product_id, image_url, is_primary) VALUES
(1, '/backend_images/tomatoes_1.jpg', TRUE),
(1, '/backend_images/tomatoes_2.jpg', FALSE),
(5, '/backend_images/milk_1.jpg', TRUE),
(9, '/backend_images/basmati_rice_1.jpg', TRUE),
(13, '/backend_images/sunflower_oil_1.jpg', TRUE),
(17, '/backend_images/bathing_soap_1.jpg', TRUE);

-- Display summary of inserted data
SELECT 'Sellers' as table_name, COUNT(*) as count FROM sellers
UNION ALL
SELECT 'Customers', COUNT(*) FROM customers
UNION ALL
SELECT 'Products', COUNT(*) FROM products
UNION ALL
SELECT 'Orders', COUNT(*) FROM orders
UNION ALL
SELECT 'Order Items', COUNT(*) FROM order_items
UNION ALL
SELECT 'Areas', COUNT(*) FROM areas
UNION ALL
SELECT 'Cart Items', COUNT(*) FROM cart
UNION ALL
SELECT 'User Sessions', COUNT(*) FROM user_sessions
UNION ALL
SELECT 'Product Images', COUNT(*) FROM product_images; 