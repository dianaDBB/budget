package com.budget.core;

import com.budget.core.dto.CategoryRuleDto;
import com.budget.core.entity.CategoryRuleEntity;
import com.budget.core.repository.CategoryRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryRuleServiceImpl implements CategoryRuleService {
    @Autowired
    private CategoryRuleRepository categoryRuleRepository;

    @Autowired
    private ConfigurationCacheService configurationCacheService;

    @Override
    public List<CategoryRuleEntity> getAllCategoryRules() {
        return configurationCacheService.getAllCategoryRules();
    }

    @Override
    public CategoryRuleDto getCategoryRules(String keyword) {
        return configurationCacheService.getCategoryRules(keyword);
    }

    @Override
    public void updateCategoryRules(List<CategoryRuleEntity> categoryRules) {
        categoryRuleRepository.deleteAll();
        categoryRuleRepository.saveAll(categoryRules);
        configurationCacheService.refreshCache();
    }
}
