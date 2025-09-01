package com.shop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "payment_records")
public class PaymentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @NotNull(message = "Payment date is required")
    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    @Column(name = "payment_method", length = 50, nullable = false)
    private String paymentMethod;

    @Column(name = "reference_number", length = 100)
    private String referenceNumber;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "paymentRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TransactionPayment> transactionPayments;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (paymentDate == null) {
            paymentDate = LocalDate.now();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<TransactionPayment> getTransactionPayments() {
        return transactionPayments;
    }

    public void setTransactionPayments(List<TransactionPayment> transactionPayments) {
        this.transactionPayments = transactionPayments;
    }
} 