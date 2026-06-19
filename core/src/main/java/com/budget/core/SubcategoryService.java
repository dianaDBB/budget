package com.budget.core;

import com.budget.core.entity.SubcategoryEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SubcategoryService {
    List<SubcategoryEntity> getAllSubcategories(String categoryName);
}