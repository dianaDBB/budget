package com.budget.helpers;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.budget.dto.EntryDto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static com.budget.helpers.FileXLSX.setCell;

public class ActivoBank {
    public static File createValidFile(List<EntryDto> entryList) throws IOException {
        File file = new File("target/test-activoBank-valid-" + System.currentTimeMillis() + ".xlsx");
        double initialBalance = 1000.00;
        LocalDate transDate = LocalDate.parse("01-May-2026", DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale.ENGLISH));
        LocalDate valueDate = LocalDate.parse("02-May-2026", DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale.ENGLISH));

        try (var workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("Transactions");

            setCell(sheet.createRow(0), 0, "HISTÓRICO DE CONTA NÚMERO 123456");
            setCell(sheet.createRow(1), 0, "Moeda: EUR");
            setCell(sheet.createRow(2), 0, "");
            setCell(sheet.createRow(3), 0, "Tipo: Todos");
            setCell(sheet.createRow(4), 0, "Data de: 01/05/2026");
            setCell(sheet.createRow(5), 0, "Data até: 31/05/2026");
            setCell(sheet.createRow(6), 0, "");

            var header = sheet.createRow(7);
            setCell(header, 0, "Data Lanc.");
            setCell(header, 1, "Data Valor");
            setCell(header, 2, "Descrição");
            setCell(header, 3, "Valor");
            setCell(header, 4, "Saldo");

            int rowIndex = 8;
            for (EntryDto entry : entryList) {
                var row = sheet.createRow(rowIndex);
                setCell(row, 0, transDate, "dd-MMMM-yyyy");
                setCell(row, 1, valueDate, "dd-MMMM-yyyy");
                setCell(row, 2, entry.description());
                setCell(row, 3, entry.amount());
                setCell(row, 4, initialBalance - entry.amount());

                initialBalance = initialBalance - entry.amount();
                rowIndex++;
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }

        return file;
    }

    public static File createInvalidFile(String invalidValue) throws IOException {
        File file = new File("target/test-activoBank-invalid-" + System.currentTimeMillis() + ".xlsx");
        LocalDate transDate = LocalDate.parse("01-May-2026", DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale.ENGLISH));
        LocalDate valueDate = LocalDate.parse("02-May-2026", DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale.ENGLISH));

        try (var workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("Invalid");

            setCell(sheet.createRow(0), 0, "Row 1");
            setCell(sheet.createRow(1), 0, "Row 2");
            setCell(sheet.createRow(2), 0, "Row 3");
            setCell(sheet.createRow(3), 0, "Row 4");
            setCell(sheet.createRow(4), 0, "Row 5");
            setCell(sheet.createRow(5), 0, "Row 6");
            setCell(sheet.createRow(6), 0, "Row 7");

            var header = sheet.createRow(7);
            setCell(header, 0, "Data Lanc.");
            setCell(header, 1, "Data Valor");
            setCell(header, 2, "Descrição");
            setCell(header, 3, "Valor");
            setCell(header, 4, "Saldo");

            var invalidRow = sheet.createRow(8);
            setCell(invalidRow, 0, transDate, "dd-MMMM-yyyy");
            setCell(invalidRow, 1, valueDate, "dd-MMMM-yyyy");
            setCell(invalidRow, 2, "Invalid value");
            setCell(invalidRow, 3, invalidValue);
            setCell(invalidRow, 4, 100.99);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }

        return file;
    }

    public static File createFileWithFirstLineAt(int dataStartRow, List<EntryDto> entryList) throws IOException {
        File file = new File("target/test-activoBank-firstLine-" + dataStartRow + "-" + System.currentTimeMillis() + ".xlsx");
        LocalDate transDate = LocalDate.parse("01-May-2026", DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale.ENGLISH));
        LocalDate valueDate = LocalDate.parse("02-May-2026", DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale.ENGLISH));
        double initialBalance = 1000.00;

        try (var workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("Transactions");

            for (int i = 0; i < dataStartRow - 1; i++) {
                setCell(sheet.createRow(i), 0, "Header row " + (i + 1));
            }

            var header = sheet.createRow(dataStartRow - 1);
            setCell(header, 0, "Data Lanc.");
            setCell(header, 1, "Data Valor");
            setCell(header, 2, "Descrição");
            setCell(header, 3, "Valor");
            setCell(header, 4, "Saldo");

            int rowIndex = dataStartRow;
            for (EntryDto entry : entryList) {
                var row = sheet.createRow(rowIndex);
                setCell(row, 0, transDate, "dd-MMMM-yyyy");
                setCell(row, 1, valueDate, "dd-MMMM-yyyy");
                setCell(row, 2, entry.description());
                setCell(row, 3, entry.amount());
                setCell(row, 4, initialBalance - entry.amount());
                initialBalance -= entry.amount();
                rowIndex++;
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }

        return file;
    }
}
