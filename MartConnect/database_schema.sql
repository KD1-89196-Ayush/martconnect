-- MartConnect Database Schema
-- This file contains all the database tables required to connect with the frontend


-- 1. CUSTOMERS TABLE
CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL,
    address TEXT,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 2. SELLERS TABLE
CREATE TABLE sellers (
    seller_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL,
    password VARCHAR(255) NOT NULL,
    shop_name VARCHAR(100) NOT NULL,
    shop_address TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 3. PRODUCTS TABLE
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    image_url VARCHAR(255),
    category VARCHAR(50) NOT NULL,
    seller_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (seller_id) REFERENCES sellers(seller_id) ON DELETE CASCADE
);

-- 4. ORDERS TABLE
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    seller_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    delivery_charge DECIMAL(10,2) DEFAULT 0.00,
    payment_status ENUM('Pending', 'Paid', 'Failed', 'Cancelled') DEFAULT 'Pending',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    transaction_id VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES sellers(seller_id) ON DELETE CASCADE
);

-- 5. ORDER_ITEMS TABLE
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price_per_unit DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- 6. AREAS TABLE (for delivery addresses)
CREATE TABLE areas (
    area_id INT PRIMARY KEY AUTO_INCREMENT,
    pincode VARCHAR(10) NOT NULL,
    area_name VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    customer_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE
);

-- 7. CART TABLE (for shopping cart functionality)
CREATE TABLE cart (
    cart_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE,
    UNIQUE KEY unique_cart_item (customer_id, product_id)
);

-- 8. CATEGORIES TABLE (for better category management)
CREATE TABLE categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 9. PRODUCT_IMAGES TABLE (for multiple product images)
CREATE TABLE product_images (
    image_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    is_primary BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);


-- Insert default categories
INSERT INTO categories (name, description) VALUES
('Fresh Vegetables', 'Fresh farm vegetables'),
('Dairy Products', 'Fresh dairy and milk products'),
('Grains', 'Rice, wheat, and pulses'),
('Edible Oil', 'Cooking oils and ghee'),
('Soaps', 'Bathing and cleaning products'),
('Grocery', 'General grocery items');

-- Sample data insertion (optional - for testing)
-- Insert sample sellers
INSERT INTO sellers (first_name, last_name, email, phone, password, shop_name, shop_address) VALUES
('Ayush', 'Dhoke', 'ayush@martconnect.com', '9876543210', 'password123', 'Ayush Electronics', 'Sitabuldi, Nagpur, Maharashtra'),
('Sneha', 'Kumar', 'sneha@fashionhub.in', '9123456780', 'sneha@123', 'FashionHub', 'MG Road, Pune, Maharashtra'),
('Ravi', 'Sharma', 'ravi@technoworld.com', '9988776655', 'ravi@2025', 'TechnoWorld', 'Powai, Mumbai, Maharashtra'),
('Priya', 'Joshi', 'priya@smartkitchen.com', '9090909090', 'kitchen@321', 'Smart Kitchen', 'Civil Lines, Nashik, Maharashtra');

-- Insert sample customers
INSERT INTO customers (first_name, last_name, email, phone, address, password) VALUES
('Rahul', 'Mehta', 'rahul.mehta@example.com', '9876501234', 'Shivaji Nagar, Pune', 'rahul123'),
('Anjali', 'Desai', 'anjali.desai@example.com', '9988776655', 'Charni Road, Mumbai', 'anjali123'),
('Vikram', 'Singh', 'vikram.singh@example.com', '9090909090', 'Indora, Nagpur', 'vikram123'),
('Neha', 'Reddy', 'neha.reddy@example.com', '9123456780', 'MG Road, Nashik', 'neha123');

-- Insert sample products
INSERT INTO products (name, price, unit, stock, image_url, description, category, seller_id) VALUES
('Wireless Mouse', 599.99, 'piece', 50, '/product_images/mouse.jpg', 'Ergonomic wireless mouse with adjustable DPI.', 'Electronics', 1),
('Yoga Mat', 899.00, 'piece', 100, '/product_images/yogamat.jpg', 'Non-slip yoga mat, eco-friendly material.', 'Fitness', 1),
('Bluetooth Speaker', 1299.50, 'piece', 35, '/product_images/speaker.jpg', 'Portable Bluetooth speaker with stereo sound.', 'Electronics', 2),
('Cooking Oil 1L', 155.75, 'bottle', 250, '/product_images/oil.jpg', 'Refined sunflower cooking oil (1L bottle).', 'Grocery', 1),
('Notebook', 45.00, 'piece', 500, '/product_images/notbook.jpg', '200-page ruled notebook for school/office use.', 'Stationery', 3);

-- Insert sample areas
INSERT INTO areas (pincode, area_name, city, state, customer_id) VALUES
('411005', 'Shivaji Nagar', 'Pune', 'Maharashtra', 1),
('400004', 'Charni Road', 'Mumbai', 'Maharashtra', 2),
('440017', 'Indora', 'Nagpur', 'Maharashtra', 3),
('422001', 'MG Road', 'Nashik', 'Maharashtra', 4); 