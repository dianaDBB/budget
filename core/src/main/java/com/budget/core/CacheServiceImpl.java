package com.budget.core;

import com.budget.core.dto.CategoryRuleDto;
import com.budget.core.entity.CategoryRuleEntity;
import com.budget.core.entity.FileConfigEntity;
import com.budget.core.repository.CategoryRuleRepository;
import com.budget.core.repository.FileConfigRepository;
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

    private volatile Map<String, FileConfigEntity> fileConfigByBankName = Map.of();
    private volatile List<FileConfigEntity> fileConfigs = List.of();
    private volatile List<CategoryRuleEntity> categoryRules = List.of();

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
}
