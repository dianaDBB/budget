package com.budget.core;

import com.budget.core.dto.BankDto;
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
    private final CategoryRuleService categoryRuleService;
    public List<BankDto> banksList;
    private Workbook excelFile;

    public File(List<BankDto> bankDtoList, CategoryRuleService categoryRuleService) {
        this.banksList = bankDtoList;
        this.categoryRuleService = categoryRuleService;
        this.excelFile = this.createExcelFile("allBanks");
    }

    public File(BankDto bankDto, CategoryRuleService categoryRuleService) {
        this.banksList = List.of(bankDto);
        this.categoryRuleService = categoryRuleService;
        this.excelFile = this.createExcelFile(bankDto.getConfig().getBankName());
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
        banksList.forEach((bankDto) -> {
            if ("XLSX".equals(bankDto.getConfig().getFileFormat())) {
                processXlsxFile(bankDto);
            } else {
                processFile(bankDto);
            }
        });

        return excelFile;
    }

    private void processXlsxFile(BankDto bankDto) {
        try {
            Workbook workbook = WorkbookFactory.create(bankDto.getFile().getInputStream());
            for (Row bankRow : workbook.getSheetAt(0)) {
                if (bankRow.getRowNum() >= bankDto.getConfig().getFirstLine()) {
                    String firstCell = bankRow.getCell(0).toString();

                    if (firstCell.isEmpty()) {
                        continue;
                    }

                    if (bankDto.getConfig().ignoreValues().contains(firstCell)) {
                        continue;
                    }

                    String bankName = bankDto.getConfig().getBankName();
                    String creditOrDebit = (bankDto.getConfig().getCreditDebitColumnPos() >= 0)
                            ? bankRow.getCell(bankDto.getConfig().getCreditDebitColumnPos()).toString()
                            : "N/A";
                    String originalDescription = bankRow.getCell(bankDto.getConfig().getDescColumnPos()).toString();

                    double amount =
                            bankDto.getConfig().getAmount(bankRow.getCell(bankDto.getConfig().getAmountColumnPos()).toString(), creditOrDebit);
                    Date date =
                            bankDto.getConfig().getFormatedDate(bankRow.getCell(bankDto.getConfig().getDateColumnPos()).toString());
                    String type = bankDto.getConfig().getType(amount, creditOrDebit, originalDescription);
                    String[] category = categoryRuleService.getCategoryRules(originalDescription);

                    // add new row, with values for each column, to Excel file
                    addRow(bankName, date, type, category[0], category[1], amount, originalDescription);
                }
            }
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processFile(BankDto bankDto) {
        InputStream inputStream;
        try {
            inputStream = bankDto.getFile().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int lineCount = 1;
        List<String> lines =
                new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().toList();
        for (String line : lines) {
            try {
                if (lineCount >= bankDto.getConfig().getFirstLine()) {
                    String[] columns = line.split(bankDto.getConfig().getDelimiter());
                    if (columns.length > 1) {
                        String bankName = bankDto.getConfig().getBankName();
                        String creditOrDebit = (bankDto.getConfig().getCreditDebitColumnPos() >= 0)
                                ? columns[bankDto.getConfig().getCreditDebitColumnPos()]
                                : "N/A";
                        String originalDescription = columns[bankDto.getConfig().getDescColumnPos()];

                        double amount =
                                bankDto.getConfig().getAmount(columns[bankDto.getConfig().getAmountColumnPos()],
                                        creditOrDebit);
                        Date date =
                                bankDto.getConfig().getFormatedDate(columns[bankDto.getConfig().getDateColumnPos()]);
                        String type = bankDto.getConfig().getType(amount, creditOrDebit, originalDescription);
                        String[] category = categoryRuleService.getCategoryRules(originalDescription);

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

    private void addRow(String bankName, Date date, String type, String category, String subCategory, double amount,
                        String originalDescription) {
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
