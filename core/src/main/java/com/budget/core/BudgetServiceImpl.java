package com.budget.core;

import com.budget.core.config.MontepioConfig;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class BudgetServiceImpl implements BudgetService {
    @Autowired
    MontepioConfig montepioConfig;

    @Override
    public Workbook montepioFileToExcel(MultipartFile multipartFile) throws IOException {
        var file = new File(montepioConfig);
        return file.bankFileToExcelFile(multipartFile);
    }
}
