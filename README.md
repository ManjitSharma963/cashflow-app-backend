# Shop Cash Flow Manager

A lightweight backend API for managing small shop cash flow and customer credits. Built with Spring Boot backend, featuring a simple REST API without authentication.

## 🚀 Features

### Core Functionality
- **Dashboard**: Real-time overview of daily sales, cash received, credit given, and pending amounts
- **Customer Management**: Add, view, and manage customer information and balances
- **Transaction Management**: Record sales, credits, and payments with customer tracking
- **Credit Tracking**: Monitor pending balances and due dates
- **Reporting**: Daily, weekly, and monthly summaries

### Technical Features
- **RESTful API**: Clean, well-structured backend API
- **No Authentication**: Simple API access without login requirements
- **Database Integration**: MySQL database with JPA/Hibernate
- **Lightweight**: Minimal dependencies, fast performance

## 🛠️ Tech Stack

### Backend
- **Java 17** with **Spring Boot 3.2.0**
- **Spring Data JPA** for data persistence
- **MySQL** database
- **Maven** for dependency management

### Frontend
- **Simple HTML**: Basic HTML page showing available API endpoints
- **No CSS/JS**: Minimal frontend, API-focused

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- API testing tool (Postman, curl, etc.)

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd mobile-app
```

### 2. Database Setup
1. Create a MySQL database:
```sql
CREATE DATABASE shop_cashflow;
```

2. Update database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build and Run
```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Access the Application
Open your browser and navigate to `http://localhost:8080`

## 📱 Usage Guide

### API Access
- **No Authentication Required**: All endpoints are publicly accessible
- **Simple Testing**: Use browser, Postman, or curl to test endpoints

### Dashboard
- **Today's Sales**: Total amount of items sold today
- **Cash Received**: Total cash payments received today
- **Credit Given**: Total credit transactions today
- **Total Pending**: Sum of all customer pending balances

### Adding Customers
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "phoneNumber": "1234567890",
    "address": "123 Main St"
  }'
```

### Recording Transactions
```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{
    "type": "SALE",
    "amount": 100.50,
    "description": "Grocery items"
  }'
```

### Managing Credits
- View pending balances on the dashboard
- Track customer-wise credit history
- Mark payments as settled when received

### Reports
- Generate daily, weekly, or monthly summaries
- View sales vs. cash vs. credit breakdowns
- Track customer transaction history

## 🔧 Configuration

### Application Properties
Key configuration options in `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/shop_cashflow
spring.datasource.username=your_username
spring.datasource.password=your_password

# Server
server.port=8080
server.servlet.context-path=/api
```

## 📊 API Endpoints

### Customers
- `GET /api/customers` - Get all customers
- `GET /api/customers/{id}` - Get customer by ID
- `GET /api/customers/search?query={query}` - Search customers
- `POST /api/customers` - Create new customer
- `PUT /api/customers/{id}` - Update customer
- `GET /api/customers/pending` - Get customers with pending balance

### Transactions
- `GET /api/transactions` - Get all transactions
- `GET /api/transactions/{id}` - Get transaction by ID
- `GET /api/transactions/customer/{customerId}` - Get customer transactions
- `POST /api/transactions` - Create new transaction
- `PUT /api/transactions/{id}/status` - Update payment status
- `GET /api/transactions/pending` - Get pending transactions

### Dashboard & Reports
- `GET /api/dashboard/today` - Get today's statistics
- `GET /api/dashboard/summary?period={period}` - Get period summary

## 🎨 Design Features

### Simple Interface
- Basic HTML page
- No complex styling or JavaScript
- API-focused design
- Easy to test and integrate

## 🔒 Security Features

- **No Authentication**: Simple API access
- **CORS Enabled**: Cross-origin requests allowed
- **Input Validation**: Request validation with Spring Validation

## 🚀 Deployment

### Production Considerations
1. **Database**: Use production-grade MySQL instance
2. **Security**: Consider adding authentication for production use
3. **HTTPS**: Enable SSL/TLS for production
4. **Environment Variables**: Use environment variables for sensitive data
5. **Logging**: Configure appropriate log levels

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/cash-flow-manager-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 🧪 Testing

### Backend Testing
```bash
mvn test
```

### API Testing
- Test all endpoints with Postman or curl
- Verify data persistence in database
- Check error handling and validation

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For issues and questions:
1. Check the existing issues
2. Create a new issue with detailed description
3. Include system information and error logs

## 🔮 Future Enhancements

- **Frontend UI**: Add a proper web interface
- **Authentication**: Implement user authentication
- **Barcode Scanning**: QR code generation for transactions
- **Inventory Management**: Track product stock levels
- **Multi-shop Support**: Manage multiple shop locations
- **Advanced Analytics**: Charts and graphs for insights
- **Export Functionality**: PDF/Excel report generation
- **Push Notifications**: Due date reminders

---

**Built with ❤️ for small business owners who need a simple, effective cash flow management solution.** 