package com.budget.core.repository;

import com.budget.core.config.CategoryRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRuleRepository extends JpaRepository<CategoryRule, UUID> {
}
