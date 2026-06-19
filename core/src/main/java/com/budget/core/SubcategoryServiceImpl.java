package com.budget.core;

import com.budget.core.entity.SubcategoryEntity;
import com.budget.core.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoryServiceImpl implements SubcategoryService {
    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private CacheServiceImpl cacheServiceImpl;

    @Override
    public List<SubcategoryEntity> getAllSubcategories(String categoryName) {
        return cacheServiceImpl.getAllSubcategories(categoryName);
    }
}
