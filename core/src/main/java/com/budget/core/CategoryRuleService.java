package com.budget.core;

import com.budget.core.entity.CategoryRuleEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoryRuleService {
    List<CategoryRuleEntity> getAllCategoryRules();

    String[] getCategoryRules(String value);

    void updateCategoryRules(List<CategoryRuleEntity> categoryRules);
}