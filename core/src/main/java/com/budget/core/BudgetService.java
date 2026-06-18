package com.budget.core;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public interface BudgetService {
    Workbook allFilesToExcel(List<String> bankNames, List<MultipartFile> files) throws IOException;

    Workbook bankFileToExcel(String bankName, MultipartFile multipartFile) throws IOException;
}