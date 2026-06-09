package com.budget.tests;

import com.budget.BaseIT;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.*;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class CryptoComFileIT extends BaseIT {
    DataFormatter formatter = new DataFormatter();

    @Test
    void shouldUploadFileAndReturnValidXlsx() throws Exception {

        File inputFile = new File("src/test/resources/CY.csv");

        Response response =
                given()
                        .multiPart("file", inputFile)
                        .contentType("multipart/form-data")
                        .when()
                        .post("/budget/budget/file/cryptoCom")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        byte[] bytes = response.asByteArray();
        assertTrue(bytes.length > 0);

        try (InputStream is = new ByteArrayInputStream(bytes);
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            assertNotNull(sheet);

            // check header

            Row header = sheet.getRow(0);
            assertNotNull(header);

            assertEquals("Origin", header.getCell(0).getStringCellValue());
            assertEquals("Date", header.getCell(1).getStringCellValue());
            assertEquals("Type", header.getCell(2).getStringCellValue());
            assertEquals("Category", header.getCell(3).getStringCellValue());
            assertEquals("Sub-category", header.getCell(4).getStringCellValue());
            assertEquals("Value", header.getCell(5).getStringCellValue());
            assertEquals("Original Description", header.getCell(6).getStringCellValue());


            // check row by row (based on input file, we now expected)
            Row row1 = sheet.getRow(1);
            assertNotNull(row1);

            String date = formatter.formatCellValue(row1.getCell(1));
            double amount = row1.getCell(5).getNumericCellValue();

            assertEquals("30-05-2026", date);
            assertEquals(-17.86, amount);
        }
    }
}