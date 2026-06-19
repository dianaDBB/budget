package com.budget.core;

import com.budget.core.dto.CategoryRuleDto;
import com.budget.core.entity.CategoryRuleEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoryRuleService {
    List<CategoryRuleDto> getAllCategoryRules();

    CategoryRuleDto getCategoryRules(String value);

    void updateCategoryRules(List<CategoryRuleEntity> categoryRules);
}