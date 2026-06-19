package com.budget.core;

import com.budget.core.dto.CategoryRuleDto;
import com.budget.core.entity.CategoryRuleEntity;
import com.budget.core.repository.CategoryRuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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
        Map<UUID, CategoryRuleEntity> requestedRulesById = categoryRules.stream()
                .filter(rule -> rule.getId() != null)
                .collect(Collectors.toMap(
                        CategoryRuleEntity::getId,
                        rule -> rule,
                        (first, second) -> {
                            throw new IllegalArgumentException("Duplicate category rule id: " + first.getId());
                        }));

        Map<UUID, CategoryRuleEntity> existingRulesById =
                categoryRuleRepository.findAllById(requestedRulesById.keySet())
                .stream()
                .collect(Collectors.toMap(CategoryRuleEntity::getId, rule -> rule));

        if (existingRulesById.size() != requestedRulesById.size()) {
            Set<UUID> missingRuleIds = new HashSet<>(requestedRulesById.keySet());
            missingRuleIds.removeAll(existingRulesById.keySet());
            throw new IllegalArgumentException("Category rules not found for ids: " + missingRuleIds);
        }

        for (Map.Entry<UUID, CategoryRuleEntity> requestedRule : requestedRulesById.entrySet()) {
            CategoryRuleEntity existingRule = existingRulesById.get(requestedRule.getKey());
            CategoryRuleEntity update = requestedRule.getValue();
            existingRule.setKeyword(update.getKeyword());
            existingRule.setCategoryId(update.getCategoryId());
            existingRule.setSubcategoryId(update.getSubcategoryId());
            existingRule.setTypeId(update.getTypeId());
        }

        List<CategoryRuleEntity> rulesToCreate = categoryRules.stream()
                .filter(rule -> rule.getId() == null)
                .toList();

        List<CategoryRuleEntity> rulesToSave = new ArrayList<>(existingRulesById.values());
        rulesToSave.addAll(rulesToCreate);

        if (!rulesToSave.isEmpty()) {
            categoryRuleRepository.saveAll(rulesToSave);
        }
        cacheServiceImpl.refreshCache();
    }
}
