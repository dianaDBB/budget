package com.budget.core;

import com.budget.core.dto.BankDto;
import com.budget.core.dto.FileLineDto;
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
    CategoryRuleService categoryRuleService;

    @Override
    public Workbook allFilesToExcel(List<String> bankNames, List<MultipartFile> files) {
        List<BankDto> banks =
                IntStream.range(0, bankNames.size()).mapToObj(i -> new BankDto(fileConfigService.getFileConfig(bankNames.get(i)), files.get(i))).toList();

        return new File(banks, categoryRuleService).bankFileToExcelFile();
    }

    @Override
    public List<FileLineDto> previewAllFilesToExcel(List<String> bankNames, List<MultipartFile> files) {
        List<BankDto> banks =
                IntStream.range(0, bankNames.size()).mapToObj(i -> new BankDto(fileConfigService.getFileConfig(bankNames.get(i)), files.get(i))).toList();

        return new File(banks, categoryRuleService).previewBankFileToExcelFile();
    }

    @Override
    public Workbook fileLinesToExcel(List<FileLineDto> fileLines) {
        return new File(List.of(), categoryRuleService).fileLinesToExcelFile(fileLines);
    }
}
