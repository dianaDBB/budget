package com.budget.core;

import com.budget.core.entity.TypeEntity;
import com.budget.core.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private CacheServiceImpl cacheServiceImpl;

    @Override
    public List<TypeEntity> getAllTypes() {
        return cacheServiceImpl.getAllTypes();
    }
}
