package com.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CustomerDto {
    private String id;
    
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @Size(min = 10, max = 15, message = "Mobile number must be between 10 and 15 digits")
    private String mobile;
    
    private String address;
    private String category = "Regular";
    private String notes;
    private BigDecimal totalDue = BigDecimal.ZERO;
    private LocalDate lastTransactionDate;
    private Boolean isActive = true;

    public CustomerDto() {}

    public CustomerDto(String id, String name, String mobile, String address, String category, 
                      String notes, BigDecimal totalDue, LocalDate lastTransactionDate, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.address = address;
        this.category = category;
        this.notes = notes;
        this.totalDue = totalDue;
        this.lastTransactionDate = lastTransactionDate;
        this.isActive = isActive;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(BigDecimal totalDue) {
        this.totalDue = totalDue;
    }

    public LocalDate getLastTransactionDate() {
        return lastTransactionDate;
    }

    public void setLastTransactionDate(LocalDate lastTransactionDate) {
        this.lastTransactionDate = lastTransactionDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
} 