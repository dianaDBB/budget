package com.budget.core;

import com.budget.core.repository.CategoryRepository;
import com.budget.core.config.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRuleRepository;

    @Override
    public List<CategoryEntity> getAllCategories() {
        return categoryRuleRepository.findAll();
    }

    @Override
    public String[] getCategory(String value) {
        return categoryRuleRepository.findAll().stream()
                .filter(rule -> value.toUpperCase().contains(rule.getKeyword().toUpperCase()))
                .findFirst()
                .map(rule -> new String[]{rule.getCategory(), rule.getSubCategory()})
                .orElse(new String[]{"", ""});
    }

    @Override
    public void updateCategories(List<CategoryEntity> rules) {
        categoryRuleRepository.deleteAll();
        categoryRuleRepository.saveAll(rules);
    }
}
