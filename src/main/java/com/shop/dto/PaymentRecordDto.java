package com.shop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentRecordDto {
    private Long id;
    
    @NotNull(message = "Customer ID is required")
    private String customerId;
    
    private String customerName;
    
    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;
    
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;
    
    @NotNull(message = "Payment method is required")
    @Size(max = 50, message = "Payment method must be less than 50 characters")
    private String paymentMethod;
    
    @Size(max = 100, message = "Reference number must be less than 100 characters")
    private String referenceNumber;
    
    private String notes;

    public PaymentRecordDto() {}

    public PaymentRecordDto(Long id, String customerId, String customerName, LocalDate paymentDate, 
                           BigDecimal amount, String paymentMethod, String referenceNumber, String notes) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.referenceNumber = referenceNumber;
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

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
} 