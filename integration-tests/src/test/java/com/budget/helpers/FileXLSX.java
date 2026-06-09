package com.budget.helpers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.fail;

public class FileXLSX {
    public static File createEmptyFile() throws IOException {
        File file = new File("target/test-empty-" + System.currentTimeMillis() + ".csv");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            // Write nothing - create empty file
        }
        return file;
    }

    public static void setCell(Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }

    public static void setCell(Row row, int column, double value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }

    public static void setCell(Row row, int column, LocalDate date, String format) {
        Cell cell = row.createCell(column);
        cell.setCellValue(date.format(DateTimeFormatter.ofPattern(format)));
    }

    public static String getCellStringValue(Workbook workbook, int rowNum, int colNum) {
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            fail("Row " + rowNum + " does not exist");
        }
        Cell cell = row.getCell(colNum);
        if (cell == null) {
            fail("Cell at row " + rowNum + ", col " + colNum + " does not exist");
        }
        return cell.getStringCellValue();
    }

    public static double getCellNumericValue(Workbook workbook, int rowNum, int colNum) {
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(rowNum);
        if (row == null) {
            fail("Row " + rowNum + " does not exist");
        }
        Cell cell = row.getCell(colNum);
        if (cell == null) {
            fail("Cell at row " + rowNum + ", col " + colNum + " does not exist");
        }
        return cell.getNumericCellValue();
    }
}
