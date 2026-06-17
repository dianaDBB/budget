package com.budget.core.config;

import com.budget.core.repository.CategoryRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryConfig {

    @Autowired
    private CategoryRuleRepository categoryRuleRepository;

    public List<CategoryRule> getRules() {
        return categoryRuleRepository.findAll();
    }

    public void setRules(List<CategoryRule> rules) {
        categoryRuleRepository.deleteAll();
        categoryRuleRepository.saveAll(rules);
    }

    public String[] getCategory(String value) {
        return categoryRuleRepository.findAll().stream()
                .filter(rule -> value.toUpperCase().contains(rule.getKeyword().toUpperCase()))
                .findFirst()
                .map(rule -> new String[]{rule.getCategory(), rule.getSubCategory()})
                .orElse(new String[]{"", ""});
    }
}
