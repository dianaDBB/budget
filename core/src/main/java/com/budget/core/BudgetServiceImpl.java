package com.budget.core;

import com.budget.core.config.ActivoBankConfig;
import com.budget.core.config.Bank;
import com.budget.core.config.CreditoAgricolaConfig;
import com.budget.core.config.CryptoComConfig;
import com.budget.core.config.MontepioConfig;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class BudgetServiceImpl implements BudgetService {
    @Autowired
    MontepioConfig montepioConfig;

    @Autowired
    ActivoBankConfig activoBankConfig;

    @Autowired
    CreditoAgricolaConfig creditoAgricolaConfig;

    @Autowired
    CryptoComConfig cryptoComConfig;

    @Override
    public Workbook allFilesToExcel(MultipartFile activoBankFile,
                                    MultipartFile creditoAgricolaFile,
                                    MultipartFile cryptoComFile) {
        var banksExcelFile = new File(List.of(
                new Bank(activoBankConfig, activoBankFile),
                new Bank(creditoAgricolaConfig, creditoAgricolaFile),
                new Bank(cryptoComConfig, cryptoComFile)
        ));
        return banksExcelFile.bankFileToExcelFile();
    }

    @Override
    public Workbook montepioFileToExcel(MultipartFile multipartFile) {
        var file = new File(new Bank(montepioConfig, multipartFile));
        return file.bankFileToExcelFile();
    }

    @Override
    public Workbook activoBankFileToExcel(MultipartFile multipartFile) {
        var file = new File(new Bank(activoBankConfig, multipartFile));
        return file.bankFileToExcelFile();
    }

    @Override
    public Workbook creditoAgricolaFileToExcel(MultipartFile multipartFile) {
        var file = new File(new Bank(creditoAgricolaConfig, multipartFile));
        return file.bankFileToExcelFile();
    }

    @Override
    public Workbook cryptoComFileToExcel(MultipartFile multipartFile) {
        var file = new File(new Bank(cryptoComConfig, multipartFile));
        return file.bankFileToExcelFile();
    }
}
