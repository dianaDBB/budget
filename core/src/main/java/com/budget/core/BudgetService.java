package com.budget.core;

import com.budget.core.dto.FileLineDto;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
public interface BudgetService {
    Workbook allFilesToExcel(List<String> bankNames, List<MultipartFile> files) throws IOException;

    List<FileLineDto> previewAllFilesToExcel(List<String> bankNames, List<MultipartFile> files) throws IOException;

    Workbook bankFileToExcel(String bankName, MultipartFile multipartFile) throws IOException;
}