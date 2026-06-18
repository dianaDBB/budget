package com.budget.core.dto;

import lombok.Data;

@Data
public class CategoryRuleDto {
    private String category;
    private String subCategory;
    private String type;

    public CategoryRuleDto(String category, String subCategory, String type) {
        this.category = category;
        this.subCategory = subCategory;
        this.type = type;
    }
}
