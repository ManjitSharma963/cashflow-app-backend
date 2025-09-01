package com.shop.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Shop Cash Flow Manager API")
                        .description("""
                                ## Shop Cash Flow Manager - Complete REST API
                                
                                This API provides comprehensive functionality for managing small shop cash flow and customer credits.
                                
                                ### 🚀 **Key Features:**
                                - **Customer Management**: Add, update, delete, and search customers
                                - **Transaction Management**: Handle credit, payment, and adjustment transactions
                                - **Payment Processing**: Process payments with transaction linking
                                - **Dashboard Analytics**: Get comprehensive business insights
                                - **Settings Management**: Configure application parameters
                                - **Data Export/Import**: Bulk data operations
                                
                                ### 📊 **Business Logic:**
                                - Customer credit limits and outstanding balances
                                - Transaction status tracking and payment allocation
                                - Category-based customer management
                                - Comprehensive reporting and analytics
                                
                                ### 🔒 **Validation Rules:**
                                - Mobile number: 10-15 digits, unique
                                - Amounts: Positive decimals, max 2 decimal places
                                - Dates: Valid dates, no future transactions
                                - Names: 2-100 characters, alphanumeric + spaces
                                
                                ### 📱 **API Endpoints:**
                                - **Base URL**: `/api`
                                - **Authentication**: None (for development)
                                - **Response Format**: JSON
                                - **Error Handling**: Standard HTTP status codes
                                
                                ### 🧪 **Testing:**
                                Use the interactive Swagger UI below to test all endpoints.
                                Sample data is provided in the database schema.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Shop Cash Flow Manager")
                                .email("support@shopcashflow.com")
                                .url("https://github.com/shopcashflow"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local Development Server"),
                        new Server()
                                .url("https://api.shopcashflow.com")
                                .description("Production Server")
                ));
    }
} 