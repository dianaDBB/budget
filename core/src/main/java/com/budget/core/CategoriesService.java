package com.budget.core;

import com.budget.core.config.CategoryEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoriesService {
    List<CategoryEntity> getAllCategories();

    String[] getCategory(String value);

    void updateCategories(List<CategoryEntity> rules);
}