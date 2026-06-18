package com.budget.core.repository;

import com.budget.core.entity.CategoryRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRuleRepository extends JpaRepository<CategoryRuleEntity, UUID> {
}
