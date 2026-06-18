package com.budget.core.dto;

import com.budget.core.entity.FileConfigEntity;
import org.springframework.web.multipart.MultipartFile;

public class BankDto {
    private FileConfigEntity config;
    private MultipartFile file;

    public BankDto(FileConfigEntity config, MultipartFile file) {
        this.config = config;
        this.file = file;
    }

    public FileConfigEntity getConfig() {
        return config;
    }

    public void setConfig(FileConfigEntity config) {
        this.config = config;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
