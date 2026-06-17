package com.budget.core.repository;

import com.budget.core.config.FileConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileConfigRepository extends JpaRepository<FileConfigEntity, UUID> {
    Optional<FileConfigEntity> findByBankName(String bankName);
}
