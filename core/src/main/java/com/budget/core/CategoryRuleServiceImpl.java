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
    private CacheServiceImpl cacheServiceImpl;

    @Override
    public List<CategoryRuleDto> getAllCategoryRules() {
        return cacheServiceImpl.getAllCategoryRules();
    }

    @Override
    public CategoryRuleDto getCategoryRules(String keyword) {
        return cacheServiceImpl.getCategoryRules(keyword);
    }

    @Override
    public void updateCategoryRules(List<CategoryRuleEntity> categoryRules) {
        categoryRuleRepository.deleteAll();
        categoryRuleRepository.saveAll(categoryRules);
        cacheServiceImpl.refreshCache();
    }
}
