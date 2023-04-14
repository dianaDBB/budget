package com.budget.core;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public interface BudgetService {
    Workbook montepioFileToExcel(MultipartFile multipartFile) throws IOException;
}