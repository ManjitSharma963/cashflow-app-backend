package com.shop.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class TransactionPaymentDto {
    @NotNull(message = "Transaction ID is required")
    private Long transactionId;
    
    @NotNull(message = "Amount applied is required")
    @Positive(message = "Amount applied must be positive")
    private BigDecimal amountApplied;

    public TransactionPaymentDto() {}

    public TransactionPaymentDto(Long transactionId, BigDecimal amountApplied) {
        this.transactionId = transactionId;
        this.amountApplied = amountApplied;
    }

    // Getters and Setters
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmountApplied() {
        return amountApplied;
    }

    public void setAmountApplied(BigDecimal amountApplied) {
        this.amountApplied = amountApplied;
    }
} 