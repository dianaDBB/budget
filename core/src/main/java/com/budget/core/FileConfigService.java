package com.budget.core;

import com.budget.core.dto.UpdateFileConfigRequestDto;
import com.budget.core.config.FileConfigEntity;

import org.springframework.stereotype.Component;

@Component
public interface FileConfigService {
    FileConfigEntity getBankFileConfig(String bankName);

    void updateBankFileConfig(String bankName, UpdateFileConfigRequestDto request);
}