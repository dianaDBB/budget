package com.budget.core;

import com.budget.core.config.BankConfig;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class File {
    private final BankConfig bankConfig;

    private Workbook excelFile;

    File(BankConfig bankConfig) {
        this.bankConfig = bankConfig;
    }

    Workbook createExcelFile() {
        // create Excel file
        excelFile = new XSSFWorkbook();

        // add sheet
        Sheet sheet = excelFile.createSheet(bankConfig.getName());

        // add header
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Origin");
        header.createCell(1).setCellValue("Date");
        header.createCell(2).setCellValue("Type");
        header.createCell(3).setCellValue("Category");
        header.createCell(4).setCellValue("Sub-category");
        header.createCell(5).setCellValue("Value");
        header.createCell(6).setCellValue("Original Description");

        DataFormat dataFormat = excelFile.createDataFormat();

        // define text style
        CellStyle textStyle = excelFile.createCellStyle();
        textStyle.setDataFormat(dataFormat.getFormat("TEXT"));

        // define date style
        CellStyle dateStyle = excelFile.createCellStyle();
        dateStyle.setDataFormat(dataFormat.getFormat("dd-mm-yyyy"));

        // set each Excel column style
        sheet.setDefaultColumnStyle(0, textStyle);
        sheet.setDefaultColumnStyle(1, dateStyle);
        sheet.setDefaultColumnStyle(2, textStyle);
        sheet.setDefaultColumnStyle(3, textStyle);
        sheet.setDefaultColumnStyle(4, textStyle);
        sheet.setDefaultColumnStyle(5, textStyle);
        sheet.setDefaultColumnStyle(6, textStyle);

        return excelFile;
    }

    Workbook bankFileToExcelFile(MultipartFile multipartFile) throws IOException {
        excelFile = createExcelFile();
        InputStream inputStream = multipartFile.getInputStream();
        AtomicInteger lineCount = new AtomicInteger();
        lineCount.set(1);
        new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                .lines()
                .forEach((line) -> {
                    try {
                        this.handleLine(line, lineCount.get());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    lineCount.getAndIncrement();
                });

        return excelFile;
    }

    private void handleLine(String line, int lineCount) throws ParseException {
        if(lineCount >= bankConfig.getFirstLine()) {
            String[] columns = line.split(bankConfig.getDelimiter());
            if (columns.length > 1) {
                int rowNum = lineCount - bankConfig.getFirstLine() + 1;
                String bankName = bankConfig.getName();
                double amount = bankConfig.getAmount(columns[bankConfig.getAmountColumnPosition()]);
                Date date = bankConfig.getDate(columns[bankConfig.getDateColumnPosition()]);
                String type = bankConfig.getType(amount);
                String originalDescription = columns[bankConfig.getDescriptionColumnPosition()];
                String category = bankConfig.getCategory();
                String subCategory = bankConfig.getSubCategory();

                // add new row, with values for each column, to Excel file
                Row row = excelFile.getSheet(bankName).createRow(rowNum);
                row.createCell(0).setCellValue(bankName);
                row.createCell(1).setCellValue(date);
                row.createCell(2).setCellValue(type);
                row.createCell(3).setCellValue(category);
                row.createCell(4).setCellValue(subCategory);
                row.createCell(5).setCellValue(amount);
                row.createCell(6).setCellValue(originalDescription);
            }
        }
    }
}
