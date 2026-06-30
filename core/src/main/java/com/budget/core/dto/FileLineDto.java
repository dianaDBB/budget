package com.budget.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class FileLineDto {
    private String bankName;
    private LocalDate date;
    private String type;
    private String category;
    private String subCategory;
    private double value;
    private String originalDescription;

    public FileLineDto(String bankName, LocalDate date, String type, String category, String subCategory,
                       double value, String originalDescription) {
        this.bankName = bankName;
        this.date = date;
        this.type = type;
        this.category = category;
        this.subCategory = subCategory;
        this.value = value;
        this.originalDescription = originalDescription;
    }
}
