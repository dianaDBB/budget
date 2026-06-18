package com.budget.core;

import com.budget.core.config.Bank;
import com.budget.core.entity.FileConfigEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    FileConfigService fileConfigService;

    @Autowired
    CategoryService categoryService;

    @Override
    public Workbook allFilesToExcel(List<String> bankNames, List<MultipartFile> files) {
        List<Bank> banks = IntStream.range(0, bankNames.size())
                .mapToObj(i -> new Bank(
                        fileConfigService.getBankFileConfig(bankNames.get(i)),
                        files.get(i)
                ))
                .toList();

        return new File(banks, categoryService).bankFileToExcelFile();
    }

    @Override
    public Workbook bankFileToExcel(String bankName, MultipartFile multipartFile) {
        FileConfigEntity config = fileConfigService.getBankFileConfig(bankName);
        return new File(new Bank(config, multipartFile), categoryService).bankFileToExcelFile();
    }
}
