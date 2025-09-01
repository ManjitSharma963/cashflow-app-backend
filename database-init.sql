-- Shop Cash Flow Manager Database Initialization Script
-- Run this script to create the database and initial setup

-- Create the database
CREATE DATABASE IF NOT EXISTS shop_cashflow
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Use the database
USE shop_cashflow;

-- The application will automatically create the tables using JPA/Hibernate
-- with the following structure:

/*
Tables that will be created automatically:

1. users - User authentication table
2. customers - Customer information and balances
3. transactions - All financial transactions (sales, credits, payments)

The application will create these tables when it starts up with:
spring.jpa.hibernate.ddl-auto=update
*/

-- Optional: Create a dedicated user for the application
-- CREATE USER 'shop_user'@'localhost' IDENTIFIED BY 'your_secure_password';
-- GRANT ALL PRIVILEGES ON shop_cashflow.* TO 'shop_user'@'localhost';
-- FLUSH PRIVILEGES;

-- Show the created database
SHOW DATABASES;

-- Verify we're using the correct database
SELECT DATABASE();

-- Note: After starting the Spring Boot application, you can initialize the default user with:
-- curl -X POST http://localhost:8080/api/auth/init 