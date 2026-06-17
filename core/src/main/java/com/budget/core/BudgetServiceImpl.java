package com.budget.core;

import com.budget.core.config.ActivoBankConfig;
import com.budget.core.config.Bank;
import com.budget.core.config.BankConfig;
import com.budget.core.config.BankConfigRequest;
import com.budget.core.config.CategoryConfig;
import com.budget.core.config.CategoryRule;
import com.budget.core.config.CreditoAgricolaConfig;
import com.budget.core.config.CryptoComConfig;
import com.budget.core.repository.FileConfigRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {
    @Autowired
    ActivoBankConfig activoBankConfig;

    @Autowired
    CreditoAgricolaConfig creditoAgricolaConfig;

    @Autowired
    CryptoComConfig cryptoComConfig;

    @Autowired
    CategoryConfig categoryConfig;

    @Autowired
    FileConfigRepository fileConfigRepository;

    @Override
    public Workbook allFilesToExcel(MultipartFile activoBankFile,
                                    MultipartFile creditoAgricolaFile,
                                    MultipartFile cryptoComFile) {
        var banksExcelFile = new File(List.of(
                new Bank(activoBankConfig, activoBankFile),
                new Bank(creditoAgricolaConfig, creditoAgricolaFile),
                new Bank(cryptoComConfig, cryptoComFile)
        ), categoryConfig);
        return banksExcelFile.bankFileToExcelFile();
    }

    @Override
    public Workbook activoBankFileToExcel(MultipartFile multipartFile) {
        var file = new File(new Bank(activoBankConfig, multipartFile), categoryConfig);
        return file.bankFileToExcelFile();
    }

    @Override
    public Workbook creditoAgricolaFileToExcel(MultipartFile multipartFile) {
        var file = new File(new Bank(creditoAgricolaConfig, multipartFile), categoryConfig);
        return file.bankFileToExcelFile();
    }

    @Override
    public Workbook cryptoComFileToExcel(MultipartFile multipartFile) {
        var file = new File(new Bank(cryptoComConfig, multipartFile), categoryConfig);
        return file.bankFileToExcelFile();
    }

    @Override
    public BankConfig getActivoBankConfig() {
        return activoBankConfig;
    }

    @Override
    public BankConfig getCreditoAgricolaConfig() {
        return creditoAgricolaConfig;
    }

    @Override
    public BankConfig getCryptoComConfig() {
        return cryptoComConfig;
    }

    @Override
    public void updateConfig(String bankName, BankConfigRequest request) {
        BankConfig config = switch (bankName.toLowerCase()) {
            case "activobank" -> activoBankConfig;
            case "creditoagricola" -> creditoAgricolaConfig;
            case "cryptocom" -> cryptoComConfig;
            default -> throw new IllegalArgumentException("Unknown bank: " + bankName);
        };
        
        config.update(request);

        fileConfigRepository.findByBankName(config.getName()).ifPresent(entity -> {
            entity.setFirstLine(config.getFirstLine());
            entity.setDateColumnPos(config.getDateColumnPosition());
            entity.setAmountColumnPos(config.getAmountColumnPosition());
            entity.setDescColumnPos(config.getDescriptionColumnPosition());
            entity.setDateformat(config.getDateFormat());
            entity.setDelimiter(config.getDelimiter());
            entity.setCreditDebitColumnPos(config.getCdColumnPosition() == -1 ? null : config.getCdColumnPosition());
            fileConfigRepository.save(entity);
        });
    }

    @Override
    public List<CategoryRule> getCategoryRules() {
        return categoryConfig.getRules();
    }

    @Override
    public void updateCategoryRules(List<CategoryRule> rules) {
        categoryConfig.setRules(rules);
    }
}
