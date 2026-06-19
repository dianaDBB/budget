package com.budget.core;

import com.budget.core.entity.TypeEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TypeService {
    List<TypeEntity> getAllTypes();
}