package com.budget.apis;

import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import static com.budget.helpers.FileXLSX.setCell;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreditoAgricolaApi {
    public static String generateFileUrl = "/budget/budget/file/creditoAgricola";

    public static File createValidFile(List<EntryDto> entryList) throws IOException {
        File file = new File("target/test-creditoAgricola-valid-" + System.currentTimeMillis() + ".xlsx");
        double initialBalance = 1000.00;
        LocalDate transDate = LocalDate.parse("01/05/2026", DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));
        LocalDate valueDate = LocalDate.parse("01/05/2026", DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH));

        try (var workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("Transactions");

            setCell(sheet.createRow(0), 0, "Operação: Consulta de Movimentos de Contas D.O.");
            setCell(sheet.createRow(1), 0, "Conta: 40367927251");
            setCell(sheet.createRow(2), 0, "De: 01/05/2026");
            setCell(sheet.createRow(3), 0, "A: 31/05/2026");
            setCell(sheet.createRow(4), 0, "");

            Row header = sheet.createRow(5);
            setCell(header, 0, "Data de Movimento");
            setCell(header, 1, "Data Valor");
            setCell(header, 2, "Descrição");
            setCell(header, 3, "Descritivo");
            setCell(header, 4, "Montante");
            setCell(header, 5, "D");
            setCell(header, 6, "Saldo Contabilístico");

            int rowIndex = 6;
            for (EntryDto entry : entryList) {
                var row = sheet.createRow(rowIndex);

                setCell(row, 0, transDate, "dd/MM/yyyy");
                setCell(row, 1, valueDate, "dd/MM/yyyy");
                setCell(row, 2, entry.description());
                setCell(row, 3, entry.description());
                setCell(row, 4, entry.amount());
                setCell(row, 5, entry.creditDebit());
                setCell(row, 6, initialBalance - entry.amount());

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
        File file = new File("target/test-creditoAgrocila-invalid-" + System.currentTimeMillis() + ".xlsx");
        LocalDate transDate = LocalDate.parse("01-May-2026", DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale.ENGLISH));
        LocalDate valueDate = LocalDate.parse("02-May-2026", DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale.ENGLISH));

        try (var workbook = new XSSFWorkbook()) {
            var sheet = workbook.createSheet("Invalid");

            setCell(sheet.createRow(0), 0, "Row 1");
            setCell(sheet.createRow(1), 0, "Row 2");
            setCell(sheet.createRow(2), 0, "Row 3");
            setCell(sheet.createRow(3), 0, "Row 4");
            setCell(sheet.createRow(4), 0, "Row 5");

            Row header = sheet.createRow(5);
            setCell(header, 0, "Data de Movimento");
            setCell(header, 1, "Data Valor");
            setCell(header, 2, "Descrição");
            setCell(header, 3, "Descritivo");
            setCell(header, 4, "Montante");
            setCell(header, 5, "D");
            setCell(header, 6, "Saldo Contabilístico");

            var invalidRow = sheet.createRow(8);
            setCell(invalidRow, 0, transDate, "dd/MM/yyyy");
            setCell(invalidRow, 1, valueDate, "dd/MM/yyyy");
            setCell(invalidRow, 2, "Invalid value");
            setCell(invalidRow, 3, invalidValue);
            setCell(invalidRow, 4, "Invalid value");
            setCell(invalidRow, 5, "D");
            setCell(invalidRow, 6, 100.99);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }

        return file;
    }

    public static XSSFWorkbook generateFile(File inputFile) throws IOException {
        Response response =
                given()
                        .multiPart("file", inputFile)
                        .contentType("multipart/form-data")
                        .when()
                        .post(generateFileUrl)
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        byte[] bytes = response.asByteArray();
        assertTrue(bytes.length > 0, "Response bytes length should be bigger that 0");

        return new XSSFWorkbook(new ByteArrayInputStream(bytes));
    }
}
