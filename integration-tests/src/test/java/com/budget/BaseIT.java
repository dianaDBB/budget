package com.budget;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(
        classes = IntegrationTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
public class BaseIT {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    public void setCell(Row row, int column, String value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }

    public void setCell(Row row, int column, double value) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
    }

    public void setCell(Row row, int column, LocalDate date, String format) {
        Cell cell = row.createCell(column);
        cell.setCellValue(date.format(DateTimeFormatter.ofPattern(format)));
    }

    public XSSFWorkbook uploadCryptoComFile(File inputFile) throws IOException {
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
        assertTrue(bytes.length > 0, "Response bytes length should be bigger that 0");

        return new XSSFWorkbook(new ByteArrayInputStream(bytes));
    }

    public XSSFWorkbook uploadActivoBankFile(File inputFile) throws IOException {
        Response response =
                given()
                        .multiPart("file", inputFile)
                        .contentType("multipart/form-data")
                        .when()
                        .post("/budget/budget/file/activoBank")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        byte[] bytes = response.asByteArray();
        assertTrue(bytes.length > 0, "Response bytes length should be bigger that 0");

        return new XSSFWorkbook(new ByteArrayInputStream(bytes));
    }

    public XSSFWorkbook uploadCreditoAgricolaFile(File inputFile) throws IOException {
        Response response =
                given()
                        .multiPart("file", inputFile)
                        .contentType("multipart/form-data")
                        .when()
                        .post("/budget/budget/file/creditoAgricola")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        byte[] bytes = response.asByteArray();
        assertTrue(bytes.length > 0, "Response bytes length should be bigger that 0");

        return new XSSFWorkbook(new ByteArrayInputStream(bytes));
    }

    public String getCellStringValue(Workbook workbook, int rowNum, int colNum) {
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

    public double getCellNumericValue(Workbook workbook, int rowNum, int colNum) {
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
