# Frontend Integration Guide - Shop Cash Flow Manager API

## 🔐 Authentication System Overview

The API now supports multi-user authentication with JWT tokens. Each user has their own isolated data (customers, transactions).

## 📋 Table of Contents

1. [Authentication Endpoints](#authentication-endpoints)
2. [Frontend Authentication Flow](#frontend-authentication-flow)
3. [API Client Setup](#api-client-setup)
4. [Customer Management APIs](#customer-management-apis)
5. [Transaction Management APIs](#transaction-management-apis)
6. [Error Handling](#error-handling)
7. [UI Components Examples](#ui-components-examples)

---

## 🔐 Authentication Endpoints

### 1. User Registration
```javascript
POST /api/auth/register
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123",
  "shopName": "John's Shop",
  "mobile": "1234567890",
  "address": "123 Main St"
}

// Response
{
  "success": true,
  "message": "User registered successfully",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "uuid",
    "name": "John Doe",
    "email": "john@example.com",
    "shopName": "John's Shop",
    "mobile": "1234567890",
    "address": "123 Main St"
  }
}
```

### 2. User Login
```javascript
POST /api/auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}

// Response
{
  "success": true,
  "message": "Login successful",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "uuid",
    "name": "John Doe",
    "email": "john@example.com",
    "shopName": "John's Shop",
    "mobile": "1234567890",
    "address": "123 Main St"
  }
}
```

### 3. Get User Profile
```javascript
GET /api/auth/profile
Authorization: Bearer <token>

// Response
{
  "id": "uuid",
  "name": "John Doe",
  "email": "john@example.com",
  "shopName": "John's Shop",
  "mobile": "1234567890",
  "address": "123 Main St",
  "createdAt": "2024-01-01T00:00:00",
  "updatedAt": "2024-01-01T00:00:00"
}
```

### 4. Update User Profile
```javascript
PUT /api/auth/profile
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "John Updated",
  "shopName": "John's Updated Shop",
  "mobile": "9876543210",
  "address": "456 New St"
}
```

### 5. Logout
```javascript
POST /api/auth/logout
Authorization: Bearer <token>

// Response
{
  "success": true,
  "message": "Logout successful"
}
```

---

## 🔄 Frontend Authentication Flow

### 1. API Client Setup

```javascript
// api.js
const API_BASE_URL = 'http://localhost:8080/api';

class ApiClient {
  constructor() {
    this.token = localStorage.getItem('authToken');
  }

  setToken(token) {
    this.token = token;
    if (token) {
      localStorage.setItem('authToken', token);
    } else {
      localStorage.removeItem('authToken');
    }
  }

  getHeaders() {
    const headers = {
      'Content-Type': 'application/json',
    };
    
    if (this.token) {
      headers.Authorization = `Bearer ${this.token}`;
    }
    
    return headers;
  }

  async request(endpoint, options = {}) {
    const url = `${API_BASE_URL}${endpoint}`;
    const config = {
      headers: this.getHeaders(),
      ...options,
    };

    try {
      const response = await fetch(url, config);
      
      if (response.status === 401) {
        // Token expired or invalid
        this.setToken(null);
        window.location.href = '/login';
        return;
      }
      
      const data = await response.json();
      
      if (!response.ok) {
        throw new Error(data.message || 'Request failed');
      }
      
      return data;
    } catch (error) {
      console.error('API Request failed:', error);
      throw error;
    }
  }

  // Authentication methods
  async register(userData) {
    const response = await this.request('/auth/register', {
      method: 'POST',
      body: JSON.stringify(userData),
    });
    
    if (response.success && response.token) {
      this.setToken(response.token);
    }
    
    return response;
  }

  async login(credentials) {
    const response = await this.request('/auth/login', {
      method: 'POST',
      body: JSON.stringify(credentials),
    });
    
    if (response.success && response.token) {
      this.setToken(response.token);
    }
    
    return response;
  }

  async logout() {
    await this.request('/auth/logout', { method: 'POST' });
    this.setToken(null);
  }

  async getProfile() {
    return await this.request('/auth/profile');
  }

  async updateProfile(userData) {
    return await this.request('/auth/profile', {
      method: 'PUT',
      body: JSON.stringify(userData),
    });
  }

  // Customer methods
  async getCustomers(filters = {}) {
    const params = new URLSearchParams(filters);
    return await this.request(`/customers?${params}`);
  }

  async createCustomer(customerData) {
    return await this.request('/customers', {
      method: 'POST',
      body: JSON.stringify(customerData),
    });
  }

  async updateCustomer(customerId, customerData) {
    return await this.request(`/customers/${customerId}`, {
      method: 'PUT',
      body: JSON.stringify(customerData),
    });
  }

  async updateCustomerTotalDue(customerId, totalDue) {
    return await this.request(`/customers/${customerId}/total-due?totalDue=${totalDue}`, {
      method: 'PUT',
    });
  }

  async deleteCustomer(customerId) {
    return await this.request(`/customers/${customerId}`, {
      method: 'DELETE',
    });
  }

  // Transaction methods
  async getTransactions(filters = {}) {
    const params = new URLSearchParams(filters);
    return await this.request(`/transactions?${params}`);
  }

  async createTransaction(transactionData) {
    return await this.request('/transactions', {
      method: 'POST',
      body: JSON.stringify(transactionData),
    });
  }

  async updateTransaction(transactionId, transactionData) {
    return await this.request(`/transactions/${transactionId}`, {
      method: 'PUT',
      body: JSON.stringify(transactionData),
    });
  }

  async deleteTransaction(transactionId) {
    return await this.request(`/transactions/${transactionId}`, {
      method: 'DELETE',
    });
  }
}

export const apiClient = new ApiClient();
```

### 2. Authentication Context (React)

```javascript
// AuthContext.js
import React, { createContext, useContext, useState, useEffect } from 'react';
import { apiClient } from './api';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  useEffect(() => {
    checkAuthStatus();
  }, []);

  const checkAuthStatus = async () => {
    try {
      if (apiClient.token) {
        const userData = await apiClient.getProfile();
        setUser(userData);
        setIsAuthenticated(true);
      }
    } catch (error) {
      console.error('Auth check failed:', error);
      logout();
    } finally {
      setLoading(false);
    }
  };

  const login = async (credentials) => {
    try {
      const response = await apiClient.login(credentials);
      if (response.success) {
        setUser(response.user);
        setIsAuthenticated(true);
        return { success: true };
      }
      return response;
    } catch (error) {
      return { success: false, message: error.message };
    }
  };

  const register = async (userData) => {
    try {
      const response = await apiClient.register(userData);
      if (response.success) {
        setUser(response.user);
        setIsAuthenticated(true);
        return { success: true };
      }
      return response;
    } catch (error) {
      return { success: false, message: error.message };
    }
  };

  const logout = async () => {
    try {
      await apiClient.logout();
    } catch (error) {
      console.error('Logout error:', error);
    } finally {
      setUser(null);
      setIsAuthenticated(false);
    }
  };

  const updateProfile = async (userData) => {
    try {
      const updatedUser = await apiClient.updateProfile(userData);
      setUser(updatedUser);
      return { success: true };
    } catch (error) {
      return { success: false, message: error.message };
    }
  };

  const value = {
    user,
    isAuthenticated,
    loading,
    login,
    register,
    logout,
    updateProfile,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
```

### 3. Protected Route Component

```javascript
// ProtectedRoute.js
import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from './AuthContext';

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated, loading } = useAuth();

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return children;
};

export default ProtectedRoute;
```

### 4. Login Component Example

```javascript
// Login.js
import React, { useState } from 'react';
import { useAuth } from './AuthContext';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    const result = await login(formData);
    
    if (result.success) {
      navigate('/dashboard');
    } else {
      setError(result.message || 'Login failed');
    }
    
    setLoading(false);
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit}>
        <h2>Login to Your Shop</h2>
        
        {error && <div className="error">{error}</div>}
        
        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>
        
        <div className="form-group">
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>
        
        <button type="submit" disabled={loading}>
          {loading ? 'Logging in...' : 'Login'}
        </button>
      </form>
    </div>
  );
};

export default Login;
```

### 5. Register Component Example

```javascript
// Register.js
import React, { useState } from 'react';
import { useAuth } from './AuthContext';
import { useNavigate } from 'react-router-dom';

const Register = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    shopName: '',
    mobile: '',
    address: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    const result = await register(formData);
    
    if (result.success) {
      navigate('/dashboard');
    } else {
      setError(result.message || 'Registration failed');
    }
    
    setLoading(false);
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div className="register-container">
      <form onSubmit={handleSubmit}>
        <h2>Register Your Shop</h2>
        
        {error && <div className="error">{error}</div>}
        
        <div className="form-group">
          <label>Name:</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </div>
        
        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>
        
        <div className="form-group">
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
            minLength="6"
          />
        </div>
        
        <div className="form-group">
          <label>Shop Name:</label>
          <input
            type="text"
            name="shopName"
            value={formData.shopName}
            onChange={handleChange}
            required
          />
        </div>
        
        <div className="form-group">
          <label>Mobile:</label>
          <input
            type="tel"
            name="mobile"
            value={formData.mobile}
            onChange={handleChange}
            required
          />
        </div>
        
        <div className="form-group">
          <label>Address:</label>
          <textarea
            name="address"
            value={formData.address}
            onChange={handleChange}
          />
        </div>
        
        <button type="submit" disabled={loading}>
          {loading ? 'Registering...' : 'Register'}
        </button>
      </form>
    </div>
  );
};

export default Register;
```

---

## 📊 Customer Management APIs

All customer APIs now require authentication and automatically filter by the authenticated user.

### Get All Customers
```javascript
const customers = await apiClient.getCustomers();
const activeCustomers = await apiClient.getCustomers({ status: true });
const searchResults = await apiClient.getCustomers({ search: 'john' });
```

### Create Customer
```javascript
const newCustomer = await apiClient.createCustomer({
  name: 'John Doe',
  mobile: '1234567890',
  address: '123 Main St',
  category: 'Regular',
  notes: 'Good customer'
});
```

### Update Customer Total Due
```javascript
const updatedCustomer = await apiClient.updateCustomerTotalDue('customer-id', 150.00);
```

---

## 💰 Transaction Management APIs

All transaction APIs now require authentication and automatically filter by the authenticated user.

### Create Transaction
```javascript
const newTransaction = await apiClient.createTransaction({
  customerId: 'customer-id',
  customerName: 'John Doe',
  transactionType: 'CREDIT', // CREDIT, PAYMENT, ADJUSTMENT
  amount: 100.00,
  description: 'Grocery items',
  date: '2024-01-15',
  status: 'PENDING', // PENDING, COMPLETED, CANCELLED
  paymentMethod: 'CASH', // CASH, BANK_TRANSFER, UPI, CHEQUE, CARD, ADJUSTMENT, OTHER
  notes: 'Customer will pay later'
});
```

---

## 🚨 Error Handling

```javascript
// Error response format
{
  "success": false,
  "message": "Error description",
  "error": "ERROR_CODE"
}

// Common error codes
- EMAIL_EXISTS: Email already registered
- INVALID_CREDENTIALS: Wrong email/password
- USER_NOT_FOUND: User not found
- CUSTOMER_EXISTS: Customer already exists
- SERVER_ERROR: Internal server error
```

---

## 🎯 Key Changes for Your UI

1. **Add Authentication Pages**: Login and Register components
2. **Update API Calls**: All existing API calls now require authentication headers
3. **Add User Context**: Manage user state across the application
4. **Protected Routes**: Ensure authenticated access to all pages
5. **Token Management**: Handle token storage and refresh
6. **User Profile**: Add user profile management
7. **Multi-tenant Data**: All data is now user-specific

---

## 🔧 Migration Steps

1. **Install Dependencies**: No additional frontend dependencies needed
2. **Update API Client**: Replace your existing API client with the authenticated version
3. **Add Authentication**: Implement login/register pages
4. **Update Routes**: Add protected route wrapper
5. **Test Authentication**: Verify login/logout flow works
6. **Update Existing Components**: Ensure all API calls work with authentication

---

## 📝 Default Admin Account

For testing, a default admin account is created:
- **Email**: `admin@shop.com`
- **Password**: `admin123`

**⚠️ Important**: Change this password in production!

---

This completes the authentication integration. Your frontend now supports multi-user authentication with isolated data per user. 