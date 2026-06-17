package com.budget.core;

import com.budget.core.config.Bank;
import com.budget.core.config.FileConfigEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    FileConfigService fileConfigService;

    @Autowired
    CategoriesService categoriesService;

    @Override
    public Workbook allFilesToExcel(MultipartFile activoBankFile,
                                    MultipartFile creditoAgricolaFile,
                                    MultipartFile cryptoComFile) {
        return new File(List.of(
                new Bank(fileConfigService.getBankFileConfig("activobank"), activoBankFile),
                new Bank(fileConfigService.getBankFileConfig("creditoagricola"), creditoAgricolaFile),
                new Bank(fileConfigService.getBankFileConfig("cryptocom"), cryptoComFile)
        ), categoriesService).bankFileToExcelFile();
    }

    @Override
    public Workbook bankFileToExcel(String bankName, MultipartFile multipartFile) {
        FileConfigEntity config = fileConfigService.getBankFileConfig(bankName);
        return new File(new Bank(config, multipartFile), categoriesService).bankFileToExcelFile();
    }
}
