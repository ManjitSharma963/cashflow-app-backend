-- Shop Cash Flow Manager - Complete Database Schema
-- Run this script to create all tables and sample data

-- Create the database
CREATE DATABASE IF NOT EXISTS shop_cashflow
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Use the database
USE shop_cashflow;

-- Drop existing tables if they exist (for clean setup)
DROP TABLE IF EXISTS transaction_payments;
DROP TABLE IF EXISTS payment_records;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS settings;

-- 1. Categories Table
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    color VARCHAR(7) DEFAULT '#3182ce',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Customers Table
CREATE TABLE customers (
    id VARCHAR(15) PRIMARY KEY,           -- Mobile number as ID
    name VARCHAR(100) NOT NULL,
    mobile VARCHAR(15) UNIQUE NOT NULL,
    address TEXT,
    category VARCHAR(50) DEFAULT 'Regular',
    notes TEXT,
    total_due DECIMAL(10,2) DEFAULT 0.00,
    last_transaction_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE
);

-- 3. Transactions Table
CREATE TABLE transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id VARCHAR(15) NOT NULL,
    transaction_type ENUM('Credit', 'Payment', 'Adjustment') NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    description TEXT,
    date DATE NOT NULL,
    status ENUM('Pending', 'Completed', 'Cancelled') DEFAULT 'Pending',
    payment_method VARCHAR(50),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    INDEX idx_customer_date (customer_id, date),
    INDEX idx_status (status),
    INDEX idx_type (transaction_type)
);

-- 4. Payment Records Table
CREATE TABLE payment_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id VARCHAR(15) NOT NULL,
    payment_date DATE NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    reference_number VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE,
    INDEX idx_customer_payment_date (customer_id, payment_date)
);

-- 5. Transaction Payments Table (Linking table)
CREATE TABLE transaction_payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_id BIGINT NOT NULL,
    payment_record_id BIGINT NOT NULL,
    amount_applied DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE,
    FOREIGN KEY (payment_record_id) REFERENCES payment_records(id) ON DELETE CASCADE,
    UNIQUE KEY unique_transaction_payment (transaction_id, payment_record_id)
);

-- 6. Settings Table
CREATE TABLE settings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    setting_key VARCHAR(100) UNIQUE NOT NULL,
    setting_value TEXT,
    setting_type ENUM('string', 'number', 'boolean', 'json') DEFAULT 'string',
    description TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert sample data

-- Insert default categories
INSERT INTO categories (name, description, color) VALUES
('Regular', 'Regular customers', '#3182ce'),
('VIP', 'VIP customers with special privileges', '#38a169'),
('Wholesale', 'Wholesale customers', '#d69e2e'),
('New', 'New customers', '#e53e3e'),
('Inactive', 'Inactive customers', '#718096');

-- Insert sample customers
INSERT INTO customers (id, name, mobile, address, category, notes, total_due, last_transaction_date) VALUES
('1234567890', 'John Doe', '1234567890', '123 Main St, City, State 12345', 'Regular', 'Good customer, pays on time', 150.00, '2024-01-15'),
('9876543210', 'Jane Smith', '9876543210', '456 Oak Ave, Town, State 54321', 'VIP', 'Premium customer, high volume', 75.50, '2024-01-14'),
('5551234567', 'Mike Johnson', '5551234567', '789 Pine Rd, Village, State 67890', 'Wholesale', 'Wholesale buyer, monthly orders', 0.00, '2024-01-10'),
('1112223333', 'Sarah Wilson', '1112223333', '321 Elm St, Borough, State 11111', 'Regular', 'New customer, first order', 200.00, '2024-01-15'),
('4445556666', 'David Brown', '4445556666', '654 Maple Dr, County, State 22222', 'New', 'Recently registered', 45.25, '2024-01-12');

-- Insert sample transactions
INSERT INTO transactions (customer_id, transaction_type, amount, description, date, status, payment_method, notes) VALUES
('1234567890', 'Credit', 100.00, 'Electronics purchase on credit', '2024-01-10', 'Pending', NULL, 'Due in 30 days'),
('1234567890', 'Payment', 50.00, 'Partial payment received', '2024-01-15', 'Completed', 'Cash', 'Customer paid in cash'),
('9876543210', 'Credit', 150.00, 'Furniture on credit', '2024-01-08', 'Pending', NULL, 'Due in 45 days'),
('9876543210', 'Payment', 75.50, 'Payment received', '2024-01-14', 'Completed', 'Bank Transfer', 'Bank transfer received'),
('5551234567', 'Credit', 500.00, 'Wholesale order', '2024-01-05', 'Completed', 'Credit Card', 'Paid immediately'),
('1112223333', 'Credit', 200.00, 'First order on credit', '2024-01-12', 'Pending', NULL, 'New customer credit'),
('4445556666', 'Credit', 45.25, 'Small purchase on credit', '2024-01-10', 'Pending', NULL, 'New customer');

-- Insert sample payment records
INSERT INTO payment_records (customer_id, payment_date, amount, payment_method, reference_number, notes) VALUES
('1234567890', '2024-01-15', 50.00, 'Cash', 'CASH001', 'Partial payment for electronics'),
('9876543210', '2024-01-14', 75.50, 'Bank Transfer', 'BANK001', 'Bank transfer for furniture'),
('5551234567', '2024-01-05', 500.00, 'Credit Card', 'CC001', 'Wholesale order payment');

-- Insert sample transaction payments (linking transactions with payments)
INSERT INTO transaction_payments (transaction_id, payment_record_id, amount_applied) VALUES
(2, 1, 50.00),  -- John's payment applied to his credit transaction
(4, 2, 75.50),  -- Jane's payment applied to her furniture transaction
(5, 3, 500.00); -- Mike's payment applied to his wholesale transaction

-- Insert default settings
INSERT INTO settings (setting_key, setting_value, setting_type, description) VALUES
('company_name', 'Shop Cash Flow Manager', 'string', 'Company name for the application'),
('currency_symbol', '₹', 'string', 'Currency symbol to display'),
('default_payment_terms', '30', 'number', 'Default payment terms in days'),
('enable_notifications', 'true', 'boolean', 'Enable/disable notifications'),
('tax_rate', '18.0', 'number', 'Default tax rate percentage'),
('max_credit_limit', '10000.00', 'number', 'Maximum credit limit for customers');

-- Show the created tables
SHOW TABLES;

-- Verify data
SELECT 'Categories' as table_name, COUNT(*) as count FROM categories
UNION ALL
SELECT 'Customers', COUNT(*) FROM customers
UNION ALL
SELECT 'Transactions', COUNT(*) FROM transactions
UNION ALL
SELECT 'Payment Records', COUNT(*) FROM payment_records
UNION ALL
SELECT 'Transaction Payments', COUNT(*) FROM transaction_payments
UNION ALL
SELECT 'Settings', COUNT(*) FROM settings;

-- Show sample data
SELECT 'Sample Customers:' as info;
SELECT id, name, mobile, category, total_due FROM customers LIMIT 3;

SELECT 'Sample Transactions:' as info;
SELECT t.id, c.name, t.transaction_type, t.amount, t.status, t.date 
FROM transactions t 
JOIN customers c ON t.customer_id = c.id 
LIMIT 3; 