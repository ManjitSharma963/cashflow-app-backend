# Shop Cash Flow Manager - Complete API Documentation

## 🔌 **Base URL:** `http://localhost:8080/api`

## 📊 **API Endpoints Overview**

### **1. Customer Management**
- `GET /api/customers` - List customers with filters
- `POST /api/customers` - Create new customer
- `GET /api/customers/:id` - Get customer details
- `PUT /api/customers/:id` - Update customer
- `DELETE /api/customers/:id` - Delete customer
- `GET /api/customers/:id/transactions` - Get customer transactions
- `GET /api/customers/search` - Search customers

### **2. Transaction Management**
- `GET /api/transactions` - List transactions with filters
- `POST /api/transactions` - Create new transaction
- `PUT /api/transactions/:id` - Update transaction
- `DELETE /api/transactions/:id` - Delete transaction
- `POST /api/transactions/:id/mark-paid` - Mark transaction as paid

### **3. Payment Management**
- `POST /api/payments` - Process payment
- `GET /api/payments` - List payments
- `GET /api/payments/:id` - Get payment details
- `PUT /api/payments/:id` - Update payment
- `DELETE /api/payments/:id` - Cancel payment
- `POST /api/payments/partial` - Process partial payment
- `POST /api/payments/lump-sum` - Process lump sum payment

### **4. Dashboard & Analytics**
- `GET /api/dashboard/summary` - Dashboard statistics
- `GET /api/dashboard/categories` - Category breakdown
- `GET /api/dashboard/outstanding` - Top outstanding customers
- `GET /api/dashboard/recent` - Recent transactions
- `GET /api/dashboard/trends` - Payment trends

### **5. Settings & Configuration**
- `GET /api/settings` - Get app settings
- `PUT /api/settings` - Update settings
- `GET /api/categories` - Get categories
- `POST /api/categories` - Create category
- `PUT /api/categories/:id` - Update category
- `DELETE /api/categories/:id` - Delete category

### **6. Data Export/Import**
- `GET /api/export/customers` - Export customers data
- `GET /api/export/transactions` - Export transactions
- `GET /api/export/payments` - Export payments
- `POST /api/import/customers` - Import customers
- `POST /api/import/transactions` - Import transactions
- `POST /api/import/payments` - Import payments

## 📝 **Detailed API Endpoints**

### **Customer Management**

#### **GET /api/customers**
**Purpose:** Retrieve all customers with optional filtering
**Method:** GET
**Query Parameters:**
- `category` (optional): Filter by customer category
- `status` (optional): Filter by active status (true/false)
- `search` (optional): Search by name or mobile
- `page` (optional): Page number for pagination
- `size` (optional): Page size for pagination

**Response:**
```json
{
  "content": [
    {
      "id": "1234567890",
      "name": "John Doe",
      "mobile": "1234567890",
      "address": "123 Main St, City",
      "category": "Regular",
      "notes": "Good customer",
      "totalDue": 150.00,
      "lastTransactionDate": "2024-01-15",
      "isActive": true
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "currentPage": 0
}
```

#### **POST /api/customers**
**Purpose:** Create new customer
**Method:** POST
**Request Body:**
```json
{
  "name": "New Customer",
  "mobile": "9998887777",
  "address": "Customer Address",
  "category": "Regular",
  "notes": "Customer notes"
}
```
**Response:** Created customer with ID

#### **GET /api/customers/:id**
**Purpose:** Get customer by ID (mobile number)
**Method:** GET
**Path Variable:** `id` (mobile number)
**Response:** Customer details

#### **PUT /api/customers/:id**
**Purpose:** Update existing customer
**Method:** PUT
**Path Variable:** `id` (mobile number)
**Request Body:** Same as POST
**Response:** Updated customer

#### **DELETE /api/customers/:id**
**Purpose:** Delete customer (only if no outstanding transactions)
**Method:** DELETE
**Path Variable:** `id` (mobile number)
**Response:** Success message

#### **GET /api/customers/:id/transactions**
**Purpose:** Get all transactions for specific customer
**Method:** GET
**Path Variable:** `id` (mobile number)
**Response:** List of customer transactions

#### **GET /api/customers/search?query={searchTerm}**
**Purpose:** Search customers by name or mobile
**Method:** GET
**Query Parameter:** `query` (search term)
**Response:** Filtered list of customers

### **Transaction Management**

#### **GET /api/transactions**
**Purpose:** Retrieve all transactions with optional filtering
**Method:** GET
**Query Parameters:**
- `customerId` (optional): Filter by customer ID
- `type` (optional): Filter by transaction type (Credit, Payment, Adjustment)
- `status` (optional): Filter by status (Pending, Completed, Cancelled)
- `startDate` (optional): Filter from date
- `endDate` (optional): Filter to date
- `page` (optional): Page number
- `size` (optional): Page size

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "customerId": "1234567890",
      "customerName": "John Doe",
      "transactionType": "Credit",
      "amount": 100.00,
      "description": "Electronics purchase",
      "date": "2024-01-10",
      "status": "Pending",
      "paymentMethod": null,
      "notes": "Due in 30 days"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "currentPage": 0
}
```

#### **POST /api/transactions**
**Purpose:** Create new transaction
**Method:** POST
**Request Body:**
```json
{
  "customerId": "1234567890",
  "transactionType": "Credit",
  "amount": 100.00,
  "description": "Transaction description",
  "date": "2024-01-15",
  "paymentMethod": "Cash",
  "notes": "Transaction notes"
}
```
**Response:** Created transaction with ID

#### **PUT /api/transactions/:id**
**Purpose:** Update existing transaction
**Method:** PUT
**Path Variable:** `id` (transaction ID)
**Request Body:** Same as POST
**Response:** Updated transaction

#### **DELETE /api/transactions/:id**
**Purpose:** Delete transaction (only if not linked to payments)
**Method:** DELETE
**Path Variable:** `id` (transaction ID)
**Response:** Success message

#### **POST /api/transactions/:id/mark-paid**
**Purpose:** Mark transaction as completed
**Method:** POST
**Path Variable:** `id` (transaction ID)
**Response:** Updated transaction

### **Payment Management**

#### **POST /api/payments**
**Purpose:** Process payment for customer
**Method:** POST
**Request Body:**
```json
{
  "customerId": "1234567890",
  "paymentDate": "2024-01-15",
  "amount": 100.00,
  "paymentMethod": "Cash",
  "referenceNumber": "REF001",
  "notes": "Payment notes",
  "transactionPayments": [
    {
      "transactionId": 1,
      "amountApplied": 100.00
    }
  ],
  "isLumpSum": false
}
```
**Response:** Created payment record

#### **GET /api/payments**
**Purpose:** List all payments with optional filtering
**Method:** GET
**Query Parameters:**
- `customerId` (optional): Filter by customer ID
- `startDate` (optional): Filter from date
- `endDate` (optional): Filter to date
- `paymentMethod` (optional): Filter by payment method
- `page` (optional): Page number
- `size` (optional): Page size

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "customerId": "1234567890",
      "customerName": "John Doe",
      "paymentDate": "2024-01-15",
      "amount": 100.00,
      "paymentMethod": "Cash",
      "referenceNumber": "REF001",
      "notes": "Payment notes"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "currentPage": 0
}
```

#### **GET /api/payments/:id**
**Purpose:** Get payment details by ID
**Method:** GET
**Path Variable:** `id` (payment ID)
**Response:** Payment details

#### **PUT /api/payments/:id**
**Purpose:** Update payment record
**Method:** PUT
**Path Variable:** `id` (payment ID)
**Request Body:** Same as POST
**Response:** Updated payment

#### **DELETE /api/payments/:id**
**Purpose:** Cancel payment (only if not applied to transactions)
**Method:** DELETE
**Path Variable:** `id` (payment ID)
**Response:** Success message

#### **POST /api/payments/partial**
**Purpose:** Process partial payment with specific transaction allocation
**Method:** POST
**Request Body:** Same as POST /api/payments with `isLumpSum: false`
**Response:** Created payment record

#### **POST /api/payments/lump-sum**
**Purpose:** Process lump sum payment for general allocation
**Method:** POST
**Request Body:** Same as POST /api/payments with `isLumpSum: true`
**Response:** Created payment record

### **Dashboard & Analytics**

#### **GET /api/dashboard/summary**
**Purpose:** Get dashboard summary statistics
**Method:** GET
**Query Parameters:**
- `period` (optional): Period for summary (today, week, month, year)
- `startDate` (optional): Custom start date
- `endDate` (optional): Custom end date

**Response:**
```json
{
  "period": "month",
  "startDate": "2024-01-01",
  "endDate": "2024-01-31",
  "totalCustomers": 150,
  "activeCustomers": 120,
  "totalTransactions": 500,
  "totalCredit": 15000.00,
  "totalPayments": 12000.00,
  "totalOutstanding": 3000.00,
  "topCategories": [
    {"category": "Regular", "count": 80, "amount": 8000.00},
    {"category": "VIP", "count": 20, "amount": 5000.00}
  ]
}
```

#### **GET /api/dashboard/categories**
**Purpose:** Get breakdown by customer categories
**Method:** GET
**Response:** Category-wise statistics

#### **GET /api/dashboard/outstanding**
**Purpose:** Get top customers with outstanding balances
**Method:** GET
**Query Parameters:**
- `limit` (optional): Number of customers to return (default: 10)

**Response:** List of customers with outstanding amounts

#### **GET /api/dashboard/recent**
**Purpose:** Get recent transactions
**Method:** GET
**Query Parameters:**
- `limit` (optional): Number of transactions to return (default: 20)

**Response:** List of recent transactions

#### **GET /api/dashboard/trends**
**Purpose:** Get payment trends over time
**Method:** GET
**Query Parameters:**
- `period` (optional): Period for trends (week, month, year)

**Response:** Payment trends data

### **Settings & Configuration**

#### **GET /api/settings**
**Purpose:** Get all application settings
**Method:** GET
**Response:** List of all settings

#### **PUT /api/settings**
**Purpose:** Update application settings
**Method:** PUT
**Request Body:**
```json
{
  "settingKey": "company_name",
  "settingValue": "New Company Name",
  "settingType": "string",
  "description": "Company name setting"
}
```
**Response:** Updated setting

#### **GET /api/categories**
**Purpose:** Get all customer categories
**Method:** GET
**Response:** List of all categories

#### **POST /api/categories**
**Purpose:** Create new customer category
**Method:** POST
**Request Body:**
```json
{
  "name": "Premium",
  "description": "Premium customers",
  "color": "#38a169"
}
```
**Response:** Created category

#### **PUT /api/categories/:id**
**Purpose:** Update existing category
**Method:** PUT
**Path Variable:** `id` (category ID)
**Request Body:** Same as POST
**Response:** Updated category

#### **DELETE /api/categories/:id**
**Purpose:** Delete category (only if no customers use it)
**Method:** DELETE
**Path Variable:** `id` (category ID)
**Response:** Success message

### **Data Export/Import**

#### **GET /api/export/customers**
**Purpose:** Export customers data
**Method:** GET
**Query Parameters:**
- `format` (optional): Export format (json, csv, excel) - default: json
- `category` (optional): Filter by category
- `status` (optional): Filter by active status

**Response:** File download or JSON data

#### **GET /api/export/transactions**
**Purpose:** Export transactions data
**Method:** GET
**Query Parameters:**
- `format` (optional): Export format (json, csv, excel) - default: json
- `startDate` (optional): Filter from date
- `endDate` (optional): Filter to date
- `type` (optional): Filter by transaction type

**Response:** File download or JSON data

#### **GET /api/export/payments**
**Purpose:** Export payments data
**Method:** GET
**Query Parameters:**
- `format` (optional): Export format (json, csv, excel) - default: json
- `startDate` (optional): Filter from date
- `endDate` (optional): Filter to date

**Response:** File download or JSON data

#### **POST /api/import/customers**
**Purpose:** Import customers from file
**Method:** POST
**Request Body:** Multipart form data with file
**Response:** Import summary

#### **POST /api/import/transactions**
**Purpose:** Import transactions from file
**Method:** POST
**Request Body:** Multipart form data with file
**Response:** Import summary

#### **POST /api/import/payments**
**Purpose:** Import payments from file
**Method:** POST
**Request Body:** Multipart form data with file
**Response:** Import summary

## 🔒 **Input Validation Rules**

### **Customer Validation:**
- **Mobile Number:** 10-15 digits, unique
- **Name:** 2-100 characters, alphanumeric + spaces
- **Category:** Predefined list or custom (max 50 chars)

### **Transaction Validation:**
- **Amount:** Positive decimal, max 2 decimal places
- **Date:** Valid date, not future date for transactions
- **Customer ID:** Required for all transaction types

### **Payment Validation:**
- **Amount:** Positive decimal, max 2 decimal places
- **Payment Method:** Required, max 50 characters
- **Reference Number:** Optional, max 100 characters

## 🧠 **Business Logic Rules**

### **Customer Rules:**
- Customer cannot be deleted if has outstanding transactions
- Mobile number must be unique across all customers
- Category must exist in categories table

### **Transaction Rules:**
- Transaction status changes affect customer total due
- Credit transactions increase customer total due
- Payment transactions decrease customer total due
- Adjustment transactions can increase or decrease total due

### **Payment Rules:**
- Payment amount cannot exceed total outstanding
- Partial payments require transaction selection or general allocation
- Payment records are immutable once created
- Transaction payments link payments to specific transactions

## 📱 **Error Handling**

### **HTTP Status Codes:**
- `200` - Success
- `201` - Created
- `400` - Bad Request (validation errors)
- `404` - Not Found
- `409` - Conflict (business rule violations)
- `500` - Internal Server Error

### **Error Response Format:**
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": [
    "Mobile number must be between 10 and 15 digits",
    "Name is required"
  ]
}
```

## 🚀 **Quick Test Commands**

```bash
# Test the API is running
curl http://localhost:8080/api/customers

# Create a customer
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Customer","mobile":"9998887777","address":"Test Address","category":"Regular"}'

# Create a transaction
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{"customerId":"9998887777","transactionType":"Credit","amount":100.00,"description":"Test credit","date":"2024-01-15"}'

# Process a payment
curl -X POST http://localhost:8080/api/payments \
  -H "Content-Type: application/json" \
  -d '{"customerId":"9998887777","paymentDate":"2024-01-15","amount":50.00,"paymentMethod":"Cash","notes":"Partial payment"}'

# Get dashboard summary
curl http://localhost:8080/api/dashboard/summary?period=month
```

This API provides a complete solution for managing shop cash flow and customer credits with comprehensive validation, business logic, and data management capabilities. 