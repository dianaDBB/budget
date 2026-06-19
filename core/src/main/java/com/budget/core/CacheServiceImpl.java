package com.budget.core;

import com.budget.core.dto.CategoryRuleDto;
import com.budget.core.entity.*;
import com.budget.core.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private FileConfigRepository fileConfigRepository;

    @Autowired
    private CategoryRuleRepository categoryRuleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private TypeRepository typeRepository;

    private volatile Map<String, FileConfigEntity> fileConfigByBankName = Map.of();
    private volatile List<FileConfigEntity> fileConfigs = List.of();
    private volatile List<CategoryRuleEntity> categoryRules = List.of();
    private volatile List<CategoryEntity> categories = List.of();
    private volatile List<SubcategoryEntity> subcategories = List.of();
    private volatile List<TypeEntity> types = List.of();

    @PostConstruct
    public void loadCache() {
        refreshCache();
    }

    @Override
    public synchronized void refreshCache() {
        List<FileConfigEntity> loadedFileConfigs = fileConfigRepository.findAll();
        fileConfigs = List.copyOf(loadedFileConfigs);
        fileConfigByBankName = loadedFileConfigs.stream().collect(LinkedHashMap::new,
                (map, config) -> map.put(config.getBankName(), config),
                Map::putAll);

        categoryRules = List.copyOf(categoryRuleRepository.findAll());

        categories = List.copyOf(categoryRepository.findAll());

        subcategories = List.copyOf(subcategoryRepository.findAll());

        types = List.copyOf(typeRepository.findAll());
    }

    @Override
    public FileConfigEntity getBankFileConfig(String bankName) {
        FileConfigEntity config = fileConfigByBankName.get(bankName);
        if (config == null) {
            throw new IllegalArgumentException("Unknown bank: " + bankName);
        }
        return config;
    }

    @Override
    public List<FileConfigEntity> getAllBankFileFormats() {
        return new ArrayList<>(fileConfigs);
    }

    @Override
    public List<CategoryRuleEntity> getAllCategoryRules() {
        return new ArrayList<>(categoryRules);
    }

    @Override
    public CategoryRuleDto getCategoryRules(String keyword) {
        String normalizedKeyword = keyword == null ? "" : keyword.toUpperCase();
        return categoryRules.stream()
                .filter(rule -> rule.getKeyword() != null && normalizedKeyword.contains(rule.getKeyword().toUpperCase()))
                .findFirst()
                .map(rule -> new CategoryRuleDto(rule.getCategory(), rule.getSubCategory(), rule.getType()))
                .orElse(new CategoryRuleDto(null, null, null));
    }

    @Override
    public List<CategoryEntity> getAllCategories() {
        return new ArrayList<>(categories);
    }

    @Override
    public List<SubcategoryEntity> getAllSubcategories(String categoryName) {
        if (categoryName == null) {
            return List.of();
        }

        return categories.stream()
                .filter(category -> categoryName.equals(category.getCategory()))
                .findFirst()
                .map(category -> subcategories.stream()
                        .filter(subcategory -> category.getId().equals(subcategory.getCategory_id()))
                        .toList())
                .orElse(List.of());
    }

    @Override
    public List<TypeEntity> getAllTypes() {
        return new ArrayList<>(types);
    }
}
