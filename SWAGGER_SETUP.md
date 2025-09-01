# 🚀 Swagger/OpenAPI Setup Guide

## 📋 **Overview**

This guide explains how to set up and use Swagger/OpenAPI documentation in your Shop Cash Flow Manager application.

## 🔧 **What's Been Added**

### **1. Dependencies Added**
- `springdoc-openapi-starter-webmvc-ui` - Swagger UI and OpenAPI 3 support
- Automatic API documentation generation

### **2. Configuration Files**
- `SwaggerConfig.java` - Custom OpenAPI configuration
- Updated `application.properties` - Swagger-specific settings
- Enhanced `index.html` - Beautiful landing page with Swagger links

## 🌐 **Accessing Swagger Documentation**

Once your application is running, you can access the API documentation at:

### **Primary Access Points:**
- **🔍 Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **📖 OpenAPI JSON:** `http://localhost:8080/api-docs`
- **📄 OpenAPI YAML:** `http://localhost:8080/api-docs.yaml`

### **Landing Page:**
- **🏠 Main Page:** `http://localhost:8080/` (redirects to `/api/`)

## 🎯 **Swagger UI Features**

### **Interactive API Testing:**
- ✅ **Try It Out:** Execute API calls directly from the browser
- ✅ **Request/Response Examples:** See exact data formats
- ✅ **Parameter Validation:** Built-in input validation
- ✅ **Authentication Support:** Ready for future auth implementation
- ✅ **Response Schemas:** Complete data structure documentation

### **API Organization:**
- 📊 **Grouped by Controllers:** Logical endpoint organization
- 🔍 **Search & Filter:** Find endpoints quickly
- 📝 **Detailed Descriptions:** Comprehensive endpoint documentation

## 🚀 **Getting Started**

### **1. Start the Application**
```bash
mvn spring-boot:run
```

### **2. Open Swagger UI**
Navigate to: `http://localhost:8080/swagger-ui.html`

### **3. Explore the API**
- Browse available endpoints by controller
- Click on any endpoint to expand details
- Use "Try it out" button to test endpoints
- View request/response schemas

## 📱 **Testing Endpoints with Swagger**

### **Example: Create a Customer**

1. **Find the endpoint:**
   - Look for `POST /api/customers` under Customer Controller

2. **Click "Try it out":**
   - This enables the interactive testing interface

3. **Fill in the request body:**
   ```json
   {
     "name": "John Doe",
     "mobile": "1234567890",
     "address": "123 Main St",
     "category": "Regular",
     "notes": "New customer"
   }
   ```

4. **Click "Execute":**
   - See the actual API response
   - View response headers and status codes

### **Example: Get Dashboard Summary**

1. **Find the endpoint:**
   - Look for `GET /api/dashboard/summary` under Dashboard Controller

2. **Click "Try it out":**
   - Add query parameters if needed (e.g., `period=month`)

3. **Click "Execute":**
   - View dashboard statistics

## 🔧 **Customization Options**

### **Swagger UI Configuration**
The following properties can be modified in `application.properties`:

```properties
# UI Customization
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.doc-expansion=none
springdoc.swagger-ui.display-request-duration=true
springdoc.swagger-ui.try-it-out-enabled=true

# Theme and Styling
springdoc.swagger-ui.syntax-highlight.theme=monokai
springdoc.swagger-ui.custom-css=.swagger-ui .topbar { display: none }
springdoc.swagger-ui.custom-site-title=Shop Cash Flow Manager API
```

### **OpenAPI Information**
Customize the API information in `SwaggerConfig.java`:

```java
.info(new Info()
    .title("Your Custom Title")
    .description("Your Custom Description")
    .version("2.0.0")
    .contact(new Contact()
        .name("Your Name")
        .email("your.email@example.com"))
)
```

## 📊 **API Documentation Structure**

### **Controllers Documented:**
- **Customer Controller** - Customer management operations
- **Transaction Controller** - Transaction CRUD operations
- **Payment Controller** - Payment processing
- **Dashboard Controller** - Analytics and reporting
- **Category Controller** - Customer categories
- **Setting Controller** - Application configuration
- **Export/Import Controller** - Data operations

### **Data Models Documented:**
- **Customer** - Customer entity with validation
- **Transaction** - Transaction entity with types
- **PaymentRecord** - Payment tracking
- **Category** - Customer categorization
- **Setting** - Application settings

## 🧪 **Testing Best Practices**

### **1. Start with Simple Endpoints**
- Test GET endpoints first (they're read-only)
- Verify the API is responding correctly

### **2. Test Data Creation**
- Create test customers and transactions
- Use realistic but test data

### **3. Test Business Logic**
- Verify validation rules work
- Test edge cases and error conditions

### **4. Use Sample Data**
- The database schema includes sample data
- Use existing customer IDs for testing

## 🚨 **Common Issues & Solutions**

### **Issue: Swagger UI not loading**
**Solution:** Check if the application is running and accessible at `http://localhost:8080`

### **Issue: Endpoints showing as "No operations defined"**
**Solution:** Ensure your controllers have proper `@RestController` and `@RequestMapping` annotations

### **Issue: Validation errors not showing**
**Solution:** Verify that `@Valid` annotations are used in controller methods

### **Issue: Custom models not appearing**
**Solution:** Ensure DTOs have proper getter/setter methods

## 🔒 **Security Considerations**

### **Current State:**
- No authentication required (development mode)
- All endpoints publicly accessible

### **Production Recommendations:**
- Implement proper authentication
- Add API key or JWT token support
- Restrict access to sensitive endpoints
- Use HTTPS in production

## 📚 **Additional Resources**

### **SpringDoc Documentation:**
- [Official SpringDoc Documentation](https://springdoc.org/)
- [OpenAPI 3 Specification](https://swagger.io/specification/)

### **Swagger UI Features:**
- [Swagger UI Documentation](https://swagger.io/tools/swagger-ui/)
- [Customization Options](https://swagger.io/docs/swagger-ui/customization/)

### **API Design Best Practices:**
- [REST API Design Guidelines](https://restfulapi.net/)
- [HTTP Status Codes](https://httpstatuses.com/)

## 🎉 **Benefits of Using Swagger**

### **For Developers:**
- ✅ **Self-Documenting API:** Always up-to-date documentation
- ✅ **Interactive Testing:** No need for external tools like Postman
- ✅ **Code Generation:** Generate client libraries automatically
- ✅ **Validation:** Built-in request/response validation

### **For API Consumers:**
- ✅ **Clear Understanding:** See exactly what each endpoint does
- ✅ **Easy Testing:** Try endpoints without writing code
- ✅ **Data Schemas:** Know exactly what data to send/receive
- ✅ **Error Handling:** Understand possible error responses

### **For Business:**
- ✅ **API Discovery:** Stakeholders can explore the API
- ✅ **Documentation:** Always current technical documentation
- ✅ **Testing:** QA teams can test API functionality
- ✅ **Integration:** Third-party developers can integrate easily

---

## 🚀 **Next Steps**

1. **Start your application** and navigate to Swagger UI
2. **Explore the endpoints** and understand the API structure
3. **Test the endpoints** using the interactive interface
4. **Customize the documentation** as needed for your use case
5. **Share the Swagger UI** with your team and stakeholders

Your API is now fully documented and ready for development and testing! 🎯 