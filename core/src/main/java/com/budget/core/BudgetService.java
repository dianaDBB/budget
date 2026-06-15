package com.budget.core;

import com.budget.core.config.BankConfig;
import com.budget.core.config.BankConfigRequest;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public interface BudgetService {
    Workbook allFilesToExcel(MultipartFile activoBankFile,
                             MultipartFile creditoAgricolaFile,
                             MultipartFile cryptoComFile) throws IOException;

    Workbook activoBankFileToExcel(MultipartFile multipartFile) throws IOException;

    Workbook creditoAgricolaFileToExcel(MultipartFile multipartFile) throws IOException;

    Workbook cryptoComFileToExcel(MultipartFile multipartFile) throws IOException;

    BankConfig getActivoBankConfig();

    BankConfig getCreditoAgricolaConfig();

    BankConfig getCryptoComConfig();

    void updateConfig(String bankName, BankConfigRequest request);
}