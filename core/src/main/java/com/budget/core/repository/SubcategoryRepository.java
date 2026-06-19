package com.budget.core.repository;

import com.budget.core.entity.SubcategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubcategoryRepository extends JpaRepository<SubcategoryEntity, UUID> {
}
