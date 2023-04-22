package com.budget.core.config;

import org.springframework.web.multipart.MultipartFile;

public class Bank {
    public BankConfig config;
    public MultipartFile file;

    public Bank(BankConfig config, MultipartFile file) {
        this.config = config;
        this.file = file;
    }
}
