package com.budget.tests;

import com.budget.BaseIT;
import com.budget.apis.BudgetApi;
import com.budget.dto.EntryDto;
import com.budget.helpers.CryptoCom;

import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.*;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static com.budget.helpers.FileXLSX.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class CryptoComFileIT extends BaseIT {
    private static final String bankName = "CryptoCom";

    @Test
    void shouldUploadFileAndReturnValidXlsx() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("Test Transaction", -50.00)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        Sheet sheet = workbook.getSheetAt(0);
        assertNotNull(sheet, "File should have at least 1 sheet");

        Row header = sheet.getRow(0);
        assertNotNull(header);

        assertEquals("Origin", header.getCell(0).getStringCellValue());
        assertEquals("Date", header.getCell(1).getStringCellValue());
        assertEquals("Type", header.getCell(2).getStringCellValue());
        assertEquals("Category", header.getCell(3).getStringCellValue());
        assertEquals("Sub-category", header.getCell(4).getStringCellValue());
        assertEquals("Value", header.getCell(5).getStringCellValue());
        assertEquals("Original Description", header.getCell(6).getStringCellValue());

        int lastRow = sheet.getLastRowNum();
        assertTrue(lastRow >= 1, "Should have at least one data row");

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeGroceryTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("COMPRA CONTINENTE Store", -45.50)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        assertEquals("Home", getCellStringValue(workbook, 1, 3));
        assertEquals("Groceries", getCellStringValue(workbook, 1, 4));

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeDiningOutTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("UBER    EATS Restaurant", -22.30)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        assertEquals("Daily_Livings", getCellStringValue(workbook, 1, 3));
        assertEquals("Dining Out", getCellStringValue(workbook, 1, 4));

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeStreamingTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("NETFLIX Subscription", -12.99)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        assertEquals("Daily_Livings", getCellStringValue(workbook, 1, 3));
        assertEquals("Streaming", getCellStringValue(workbook, 1, 4));

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeHealthTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("FARMACIA Pharmacy", -18.50)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        assertEquals("Daily_Livings", getCellStringValue(workbook, 1, 3));
        assertEquals("Health", getCellStringValue(workbook, 1, 4));

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeGymTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("BALTAREJO Gym", -49.99)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        assertEquals("Daily_Livings", getCellStringValue(workbook, 1, 3));
        assertEquals("Gym", getCellStringValue(workbook, 1, 4));

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeInternetTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("VODAFONE Internet", -35.99)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        assertEquals("Utilities", getCellStringValue(workbook, 1, 3));
        assertEquals("Internet", getCellStringValue(workbook, 1, 4));

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizePhoneTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("NOS COM Phone", -40.50)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        assertEquals("Utilities", getCellStringValue(workbook, 1, 3));
        assertEquals("Phone", getCellStringValue(workbook, 1, 4));

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeFuelTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("GALP Fuel Station", -60.00)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        assertEquals("Car", getCellStringValue(workbook, 1, 3));
        assertEquals("Fuel", getCellStringValue(workbook, 1, 4));

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeTollsTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("VIA VERDE Toll", -8.50)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        assertEquals("Car", getCellStringValue(workbook, 1, 3));
        assertEquals("Tolls", getCellStringValue(workbook, 1, 4));

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeContractorTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("IDEIAS DECIMAIS Contractor", -150.00)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        assertEquals("House_Construction", getCellStringValue(workbook, 1, 3));
        assertEquals("Contractor", getCellStringValue(workbook, 1, 4));

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeSuppliesTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("COMPRA STAPLES Supplies", -25.75)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        assertEquals("Job´s", getCellStringValue(workbook, 1, 3));
        assertEquals("Supplies", getCellStringValue(workbook, 1, 4));

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeFeesTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("COMISSÃO S/ Bank Fee", -2.50)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        assertEquals("Bank", getCellStringValue(workbook, 1, 3));
        assertEquals("Fees", getCellStringValue(workbook, 1, 4));

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeLoanTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("TRF.     0000351 00938121242", -250.00)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        assertEquals("Home", getCellStringValue(workbook, 1, 3));
        assertEquals("Loan", getCellStringValue(workbook, 1, 4));

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldNotCategorizeUnknownTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("Unknown Random Purchase", -99.99)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        assertEquals("", getCellStringValue(workbook, 1, 3));
        assertEquals("", getCellStringValue(workbook, 1, 4));

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldClassifyIncomeTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("EUR Deposit Income", 1000.00)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        String type = getCellStringValue(workbook, 1, 2);
        double amount = getCellNumericValue(workbook, 1, 5);

        assertTrue("Income".equals(type) || "TrasnferInHouse".equals(type), "Type should be Income or Transfer");
        assertTrue(amount >= 0, "Amount should be positive for income");

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldClassifyExpenseTransaction() throws Exception {
        File inputFile = CryptoCom.createValidFile(List.of(new EntryDto("Random Purchase Expense", -75.50)));

        Workbook workbook = BudgetApi.generateBankFile(bankName, inputFile);

        String type = getCellStringValue(workbook, 1, 2);
        double amount = getCellNumericValue(workbook, 1, 5);

        assertEquals("Expense", type, "Type should be Expense");
        assertTrue(amount <= 0, "Amount should be negative for expense");

        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldReturnOkWhenFileIsEmpty() throws Exception {
        File emptyFile = createEmptyFile();

        Response response = given()
                .multiPart("file", emptyFile)
                .contentType("multipart/form-data")
                .when()
                .post(BudgetApi.generateFileUrl, bankName);

        int statusCode = response.statusCode();
        assertEquals(200, statusCode, "Should return error status code (200), got: " + statusCode);

        assertTrue(emptyFile.delete(), "Fail to delete test file");
    }

    @Test
    void shouldReturnErrorWhenFileHasInvalidFormat() throws Exception {
        File invalidFile = CryptoCom.createInvalidFile("this is not a number");

        Response response = given()
                .multiPart("file", invalidFile)
                .contentType("multipart/form-data")
                .when()
                .post(BudgetApi.generateFileUrl, bankName);

        int statusCode = response.statusCode();
        assertEquals(500, statusCode, "Should return error status code (500), got: " + statusCode);

        assertTrue(invalidFile.delete(), "Fail to delete test file");
    }
}