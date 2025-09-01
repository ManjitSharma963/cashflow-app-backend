package com.shop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PaymentRequestDto {
    @NotNull(message = "Customer ID is required")
    private String customerId;
    
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
    
    // For partial payments - specific transactions to pay
    private List<TransactionPaymentDto> transactionPayments;
    
    // For lump sum payments - general allocation
    private Boolean isLumpSum = false;

    public PaymentRequestDto() {}

    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public List<TransactionPaymentDto> getTransactionPayments() {
        return transactionPayments;
    }

    public void setTransactionPayments(List<TransactionPaymentDto> transactionPayments) {
        this.transactionPayments = transactionPayments;
    }

    public Boolean getIsLumpSum() {
        return isLumpSum;
    }

    public void setIsLumpSum(Boolean isLumpSum) {
        this.isLumpSum = isLumpSum;
    }
} 