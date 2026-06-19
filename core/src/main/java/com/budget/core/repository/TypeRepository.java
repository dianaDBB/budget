package com.budget.core.repository;

import com.budget.core.entity.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TypeRepository extends JpaRepository<TypeEntity, UUID> {
}
