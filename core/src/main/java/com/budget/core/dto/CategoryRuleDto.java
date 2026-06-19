package com.budget.core.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryRuleDto {
    private String category;
    private UUID category_id;
    private String subCategory;
    private UUID subCategory_id;
    private String type;
    private UUID type_id;

    public CategoryRuleDto(String category, UUID category_id, String subCategory, UUID subCategory_id, String type,
                           UUID type_id) {
        this.category = category;
        this.category_id = category_id;
        this.subCategory = subCategory;
        this.subCategory_id = subCategory_id;
        this.type = type;
        this.type_id = type_id;
    }
}
