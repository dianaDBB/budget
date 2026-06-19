package com.budget.core;

import com.budget.core.entity.CategoryEntity;
import com.budget.core.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CacheServiceImpl cacheServiceImpl;

    @Override
    public List<CategoryEntity> getAllCategories() {
        return cacheServiceImpl.getAllCategories();
    }
}
