package com.budget.core;

import com.budget.core.dto.CategoryRuleDto;
import com.budget.core.entity.CategoryEntity;
import com.budget.core.entity.CategoryRuleEntity;
import com.budget.core.entity.FileConfigEntity;
import com.budget.core.entity.SubcategoryEntity;
import com.budget.core.entity.TypeEntity;
import com.budget.core.repository.CategoryRepository;
import com.budget.core.repository.CategoryRuleRepository;
import com.budget.core.repository.FileConfigRepository;
import com.budget.core.repository.SubcategoryRepository;
import com.budget.core.repository.TypeRepository;
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
    public List<CategoryRuleDto> getAllCategoryRules() {
        List<CategoryRuleDto> categoryRuleList = new ArrayList<>();

        for (CategoryRuleEntity categoryRuleEntity : categoryRules) {
            String category = categories.stream()
                    .filter(cat -> cat.getId().equals(categoryRuleEntity.getCategoryId()))
                    .map(CategoryEntity::getCategory)
                    .findFirst()
                    .orElse(null);

            String subcategory = subcategories.stream()
                    .filter(sc -> sc.getId().equals(categoryRuleEntity.getSubcategoryId()))
                    .map(SubcategoryEntity::getSubcategory)
                    .findFirst()
                    .orElse(null);

            String type = types.stream()
                    .filter(tc -> tc.getId().equals(categoryRuleEntity.getTypeId()))
                    .map(TypeEntity::getType)
                    .findFirst()
                    .orElse(null);

            categoryRuleList.add(new CategoryRuleDto(categoryRuleEntity.getKeyword(), category,
                    categoryRuleEntity.getCategoryId(), subcategory, categoryRuleEntity.getSubcategoryId(), type,
                    categoryRuleEntity.getTypeId()));
        }

        return categoryRuleList;
    }

    @Override
    public CategoryRuleDto getCategoryRules(String keyword) {
        String normalizedKeyword = keyword == null ? "" : keyword.toUpperCase();

        CategoryRuleEntity categoryRuleEntity = categoryRules.stream()
                .filter(rule -> rule.getKeyword() != null && normalizedKeyword.contains(rule.getKeyword().toUpperCase()))
                .findFirst().orElse(null);

        if (categoryRuleEntity == null) {
            return new CategoryRuleDto(null, null, null, null, null, null, null);
        }

        String category = categories.stream()
                .filter(cat -> cat.getId().equals(categoryRuleEntity.getCategoryId()))
                .map(CategoryEntity::getCategory)
                .findFirst()
                .orElse(null);

        String subcategory = subcategories.stream()
                .filter(sc -> sc.getId().equals(categoryRuleEntity.getSubcategoryId()))
                .map(SubcategoryEntity::getSubcategory)
                .findFirst()
                .orElse(null);

        String type = types.stream()
                .filter(tc -> tc.getId().equals(categoryRuleEntity.getTypeId()))
                .map(TypeEntity::getType)
                .findFirst()
                .orElse(null);

        return new CategoryRuleDto(categoryRuleEntity.getKeyword(), category, categoryRuleEntity.getCategoryId(),
                subcategory, categoryRuleEntity.getSubcategoryId(), type, categoryRuleEntity.getTypeId());
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
