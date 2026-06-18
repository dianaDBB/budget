package com.budget.core.dto;

import lombok.Data;

@Data
public class FileLineDto {
    private String bankName;
    private String date;
    private String type;
    private String category;
    private String subCategory;
    private double value;
    private String originalDescription;

    public FileLineDto(String bankName, String date, String type, String category, String subCategory, double value,
                       String originalDescription) {
        this.bankName = bankName;
        this.date = date;
        this.type = type;
        this.category = category;
        this.subCategory = subCategory;
        this.value = value;
        this.originalDescription = originalDescription;
    }
}
