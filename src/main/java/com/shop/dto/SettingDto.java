package com.shop.dto;

import com.shop.entity.Setting.SettingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SettingDto {
    private Integer id;
    
    @NotBlank(message = "Setting key is required")
    @Size(max = 100, message = "Setting key must be less than 100 characters")
    private String settingKey;
    
    private String settingValue;
    private SettingType settingType = SettingType.STRING;
    private String description;

    public SettingDto() {}

    public SettingDto(Integer id, String settingKey, String settingValue, SettingType settingType, String description) {
        this.id = id;
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.settingType = settingType;
        this.description = description;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSettingKey() {
        return settingKey;
    }

    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }

    public String getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    public SettingType getSettingType() {
        return settingType;
    }

    public void setSettingType(SettingType settingType) {
        this.settingType = settingType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
} 