package com.budget.core;

import com.budget.core.dto.CategoryRuleDto;
import com.budget.core.repository.CategoryRuleRepository;
import com.budget.core.entity.CategoryRuleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryRuleServiceImpl implements CategoryRuleService {
    @Autowired
    private CategoryRuleRepository categoryRuleRepository;

    @Override
    public List<CategoryRuleEntity> getAllCategoryRules() {
        return categoryRuleRepository.findAll();
    }

    @Override
    public CategoryRuleDto getCategoryRules(String keyword) {
        return categoryRuleRepository.findAll().stream()
                .filter(rule -> keyword.toUpperCase().contains(rule.getKeyword().toUpperCase()))
                .findFirst()
                .map(rule -> new CategoryRuleDto(rule.getCategory(), rule.getSubCategory(), rule.getType()))
                .orElse(new CategoryRuleDto(null, null, null));
    }

    @Override
    public void updateCategoryRules(List<CategoryRuleEntity> categoryRules) {
        categoryRuleRepository.deleteAll();
        categoryRuleRepository.saveAll(categoryRules);
    }
}
