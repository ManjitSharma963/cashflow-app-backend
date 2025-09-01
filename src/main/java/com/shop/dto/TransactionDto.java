package com.shop.dto;

import com.shop.entity.Transaction.TransactionType;
import com.shop.entity.Transaction.TransactionStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionDto {
    private Long id;
    
    @NotNull(message = "Customer ID is required")
    private String customerId;
    
    private String customerName;
    
    @NotNull(message = "Transaction type is required")
    private TransactionType transactionType;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    private String description;
    
    @NotNull(message = "Date is required")
    private LocalDate date;
    
    private TransactionStatus status = TransactionStatus.PENDING;
    private String paymentMethod;
    private String notes;

    public TransactionDto() {}

    public TransactionDto(Long id, String customerId, String customerName, TransactionType transactionType, 
                         BigDecimal amount, String description, LocalDate date, TransactionStatus status, 
                         String paymentMethod, String notes) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.notes = notes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
} 