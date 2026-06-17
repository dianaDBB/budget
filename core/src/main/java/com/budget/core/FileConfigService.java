package com.budget.core;

import com.budget.core.config.FileConfigEntity;
import com.budget.core.config.BankConfigRequest;
import org.springframework.stereotype.Component;

@Component
public interface FileConfigService {
    FileConfigEntity getBankFileConfig(String bankName);

    void updateBankFileConfig(String bankName, BankConfigRequest request);
}