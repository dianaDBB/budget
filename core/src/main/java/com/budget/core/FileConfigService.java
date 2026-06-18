package com.budget.core;

import com.budget.core.dto.UpdateFileConfigRequestDto;
import com.budget.core.entity.FileConfigEntity;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FileConfigService {
    FileConfigEntity getBankFileConfig(String bankName);

    List<FileConfigEntity> getAllBankFileFormats();

    void updateBankFileConfig(String bankName, UpdateFileConfigRequestDto request);
}