package com.budget.core;

import com.budget.core.config.Bank;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
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
            if (bank.config.getName().equals("CreditoAgricola") || bank.config.getName().equals("ActivoBank")) {
                processXlsxFile(bank);
            } else {
                processFile(bank);
            }
        });

        return excelFile;
    }

    private void processXlsxFile(Bank bank) {
        try {
            Workbook workbook = WorkbookFactory.create(bank.file.getInputStream());
            for (Row bankRow : workbook.getSheetAt(0)) {
                if (bankRow.getRowNum() >= bank.config.getFirstLine()) {
                    String firstCell = bankRow.getCell(0).toString();

                    if (firstCell.isEmpty()) {
                        continue;
                    }

                    if (bank.config.ignoreValues().contains(firstCell)) {
                        continue;
                    }

                    String bankName = bank.config.getName();
                    String creditOrDebit = (bank.config.getCdColumnPosition() >= 0)
                            ? bankRow.getCell(bank.config.getCdColumnPosition()).toString()
                            : "N/A";
                    String originalDescription = bankRow.getCell(bank.config.getDescriptionColumnPosition()).toString();

                    double amount = bank.config.getAmount(bankRow.getCell(bank.config.getAmountColumnPosition()).toString(), creditOrDebit);
                    Date date = bank.config.getDate(bankRow.getCell(bank.config.getDateColumnPosition()).toString());
                    String type = bank.config.getType(amount, creditOrDebit, originalDescription);
                    String[] category = bank.config.getCategory(originalDescription);

                    // add new row, with values for each column, to Excel file
                    addRow(bankName, date, type, category[0], category[1], amount, originalDescription);
                }
            }
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processFile(Bank bank) {
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
                if (lineCount >= bank.config.getFirstLine()) {
                    String[] columns = line.split(bank.config.getDelimiter());
                    if (columns.length > 1) {
                        String bankName = bank.config.getName();
                        String creditOrDebit = (bank.config.getCdColumnPosition() >= 0)
                                ? columns[bank.config.getCdColumnPosition()]
                                : "N/A";
                        String originalDescription = columns[bank.config.getDescriptionColumnPosition()];

                        double amount = bank.config.getAmount(columns[bank.config.getAmountColumnPosition()], creditOrDebit);
                        Date date = bank.config.getDate(columns[bank.config.getDateColumnPosition()]);
                        String type = bank.config.getType(amount, creditOrDebit, originalDescription);
                        String[] category = bank.config.getCategory(originalDescription);

                        // add new row, with values for each column, to Excel file
                        addRow(bankName, date, type, category[0], category[1], amount, originalDescription);
                    }
                }


            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            lineCount++;
        }
    }

    private void addRow(String bankName, Date date, String type, String category, String subCategory, double amount, String originalDescription) {
        int lastRow = excelFile.getSheetAt(0).getLastRowNum();
        Row row = excelFile.getSheetAt(0).createRow(lastRow + 1);
        row.createCell(0).setCellValue(bankName);
        row.createCell(1).setCellValue(date);
        row.createCell(2).setCellValue(type);
        row.createCell(3).setCellValue(category);
        row.createCell(4).setCellValue(subCategory);
        row.createCell(5).setCellValue(amount);
        row.createCell(6).setCellValue(originalDescription);
    }
}
