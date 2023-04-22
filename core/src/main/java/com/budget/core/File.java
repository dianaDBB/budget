package com.budget.core;

import com.budget.core.config.Bank;
import com.budget.core.config.BankConfig;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class File {
    public List<Bank> banksList;
    private Workbook excelFile;

    public File(List<Bank> bankList) {
        this.banksList = bankList;
        this.excelFile = this.createExcelFile("allBanks");
    }

    public File(Bank bank) {
        banksList = List.of(bank);
        excelFile = this.createExcelFile(bank.config.getName());
    }

    Workbook createExcelFile(String sheetName) {
        // create Excel file
        excelFile = new XSSFWorkbook();

        // add sheet
        Sheet sheet = excelFile.createSheet(sheetName);

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

    Workbook bankFileToExcelFile() {
        banksList.forEach((bank) -> {
            InputStream inputStream;
            try {
                inputStream = bank.file.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            int lineCount = 1;
            List<String> lines = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().toList();
            for (String line : lines) {
                try {
                    this.handleLine(bank.config, line, lineCount);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                lineCount++;
            }
        });
        return excelFile;
    }

    private void handleLine(BankConfig bankConfig, String line, int lineCount) throws ParseException {
        if(lineCount >= bankConfig.getFirstLine()) {
            String[] columns = line.split(bankConfig.getDelimiter());
            if (columns.length > 1) {
                String bankName = bankConfig.getName();
                double amount = bankConfig.getAmount(columns[bankConfig.getAmountColumnPosition()]);
                Date date = bankConfig.getDate(columns[bankConfig.getDateColumnPosition()]);
                String type = bankConfig.getType(amount);
                String originalDescription = columns[bankConfig.getDescriptionColumnPosition()];
                String category = bankConfig.getCategory();
                String subCategory = bankConfig.getSubCategory();

                // add new row, with values for each column, to Excel file
                int lastRow = excelFile.getSheet(bankName).getLastRowNum();
                Row row = excelFile.getSheet(bankName).createRow(lastRow + 1);
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
