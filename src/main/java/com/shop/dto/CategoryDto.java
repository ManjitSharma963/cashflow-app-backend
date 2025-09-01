package com.shop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CategoryDto {
    private Integer id;
    
    @NotBlank(message = "Category name is required")
    @Size(max = 50, message = "Category name must be less than 50 characters")
    private String name;
    
    private String description;
    private String color = "#3182ce";
    private Boolean isActive = true;

    public CategoryDto() {}

    public CategoryDto(Integer id, String name, String description, String color, Boolean isActive) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.isActive = isActive;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
} 