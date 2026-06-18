package com.budget.core.dto;

import com.budget.core.entity.FileConfigEntity;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BankDto {
    private FileConfigEntity config;
    private MultipartFile file;

    public BankDto(FileConfigEntity config, MultipartFile file) {
        this.config = config;
        this.file = file;
    }
}
