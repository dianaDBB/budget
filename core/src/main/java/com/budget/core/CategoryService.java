package com.budget.core;

import com.budget.core.entity.CategoryEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CategoryService {
    List<CategoryEntity> getAllCategories();
}