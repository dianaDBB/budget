package com.budget.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "categories")
public class CategoryConfig {
    private List<CategoryRule> rules = new ArrayList<>();

    public List<CategoryRule> getRules() {
        return rules;
    }

    public void setRules(List<CategoryRule> rules) {
        this.rules = rules;
    }

    public String[] getCategory(String value) {
        return rules.stream()
                .filter(rule -> value.toUpperCase().contains(rule.getKeyword().toUpperCase()))
                .findFirst()
                .map(rule -> new String[]{rule.getCategory(), rule.getSubCategory()})
                .orElse(new String[]{"", ""});
    }
}
