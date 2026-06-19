package com.budget.core;

import com.budget.core.dto.CategoryRuleDto;
import com.budget.core.entity.CategoryEntity;
import com.budget.core.entity.CategoryRuleEntity;
import com.budget.core.entity.FileConfigEntity;
import com.budget.core.entity.SubcategoryEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CacheService {
    void refreshCache();

    FileConfigEntity getBankFileConfig(String bankName);

    List<FileConfigEntity> getAllBankFileFormats();

    List<CategoryRuleEntity> getAllCategoryRules();

    CategoryRuleDto getCategoryRules(String keyword);

    List<CategoryEntity> getAllCategories();

    List<SubcategoryEntity> getAllSubcategories(String categoryName);
}