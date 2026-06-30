package com.budget.core;

import com.budget.core.dto.AddFileConfigRequestDto;
import com.budget.core.dto.UpdateFileConfigRequestDto;
import com.budget.core.entity.FileConfigEntity;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FileConfigService {
    FileConfigEntity getFileConfig(String bankName);

    List<FileConfigEntity> getAllFilesConfigs();

    void updateFileConfig(String bankName, UpdateFileConfigRequestDto request);

    void addFileConfig(AddFileConfigRequestDto request);

    void deleteFileConfig(String bankName);
}