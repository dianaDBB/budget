package com.budget.core.config;

import org.springframework.web.multipart.MultipartFile;

public class Bank {
    public FileConfigEntity config;
    public MultipartFile file;

    public Bank(FileConfigEntity config, MultipartFile file) {
        this.config = config;
        this.file = file;
    }
}
