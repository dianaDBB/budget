package com.budget.tests;

import com.budget.BaseIT;
import com.budget.apis.CreditoAgricolaApi;
import com.budget.apis.Entry;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static com.budget.apis.CreditoAgricolaApi.*;
import static com.budget.helpers.FileXLSX.*;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class CreditoAgricolaFileIT extends BaseIT {
    @Test
    void shouldUploadFileAndReturnValidXlsx() throws Exception {
        // Given I have an input file with a test transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("Test Transaction", -50.00)));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the file has the correct structure and headers
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

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeGroceryTransaction() throws Exception {
        // Given I have an input file with a grocery transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("COMPRA CONTINENTE Store", 45.50, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Home", getCellStringValue(workbook, 1, 3));
        assertEquals("Groceries", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeDiningOutTransaction() throws Exception {
        // Given I have an input file with a dining transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("UBER    EATS Restaurant", 22.30, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Daily_Livings", getCellStringValue(workbook, 1, 3));
        assertEquals("Dining Out", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeStreamingTransaction() throws Exception {
        // Given I have an input file with a streaming subscription transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("NETFLIX Subscription", 12.99, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Daily_Livings", getCellStringValue(workbook, 1, 3));
        assertEquals("Streaming", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeHealthTransaction() throws Exception {
        // Given I have an input file with a pharmacy transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("FARMACIA Pharmacy", 18.50, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Daily_Livings", getCellStringValue(workbook, 1, 3));
        assertEquals("Health", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeGymTransaction() throws Exception {
        // Given I have an input file with a gym transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("BALTAREJO Gym", 49.99, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Daily_Livings", getCellStringValue(workbook, 1, 3));
        assertEquals("Gym", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeInternetTransaction() throws Exception {
        // Given I have an input file with an internet transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("VODAFONE Internet", 35.99, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Utilities", getCellStringValue(workbook, 1, 3));
        assertEquals("Internet", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizePhoneTransaction() throws Exception {
        // Given I have an input file with a phone transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("NOS COM Phone", 40.50, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Utilities", getCellStringValue(workbook, 1, 3));
        assertEquals("Phone", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeFuelTransaction() throws Exception {
        // Given I have an input file with a fuel transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("GALP Fuel Station", 60.00, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Car", getCellStringValue(workbook, 1, 3));
        assertEquals("Fuel", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeTollsTransaction() throws Exception {
        // Given I have an input file with a tolls transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("VIA VERDE Toll", 8.50, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Car", getCellStringValue(workbook, 1, 3));
        assertEquals("Tolls", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeContractorTransaction() throws Exception {
        // Given I have an input file with a contractor transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("IDEIAS DECIMAIS Contractor", 150.00, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("House_Construction", getCellStringValue(workbook, 1, 3));
        assertEquals("Contractor", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeSuppliesTransaction() throws Exception {
        // Given I have an input file with an office supplies transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("COMPRA STAPLES Supplies", 25.75, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Job´s", getCellStringValue(workbook, 1, 3));
        assertEquals("Supplies", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeFeesTransaction() throws Exception {
        // Given I have an input file with a bank fees transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("COMISSÃO S/ Bank Fee", 2.50, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Bank", getCellStringValue(workbook, 1, 3));
        assertEquals("Fees", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeLoanTransaction() throws Exception {
        // Given I have an input file with a loan transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("TRF.     0000351 00938121242", 250.00, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Home", getCellStringValue(workbook, 1, 3));
        assertEquals("Loan", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldNotCategorizeUnknownTransaction() throws Exception {
        // Given I have an input file with an unknown transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("Unknown Random Purchase", 99.99, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file has empty Category / Sub-Category
        assertEquals("", getCellStringValue(workbook, 1, 3));
        assertEquals("", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldClassifyIncomeTransaction() throws Exception {
        // Given I have an input file with an income transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("EUR Deposit Income", 1000.00, "C")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file classifies it as Income or Transfer with positive amount
        String type = getCellStringValue(workbook, 1, 2);
        double amount = getCellNumericValue(workbook, 1, 5);

        assertTrue("Income".equals(type) || "TrasnferInHouse".equals(type), "Type should be Income or Transfer");
        assertTrue(amount >= 0, "Amount should be positive for income");

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldClassifyExpenseTransaction() throws Exception {
        // Given I have an input file with an expense transaction
        File inputFile = createCreditoAgricolaFile(List.of(new Entry("Random Purchase Expense", 75.50, "D")));

        // When I generate the budget file
        Workbook workbook = uploadCreditoAgricolaFile(inputFile);

        // Then the generated file classifies it as Expense with negative amount
        String type = getCellStringValue(workbook, 1, 2);
        double amount = getCellNumericValue(workbook, 1, 5);

        assertEquals("Expense", type, "Type should be Expense");
        assertTrue(amount <= 0, "Amount should be negative for expense");

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldReturnErrorWhenFileIsEmpty() throws Exception {
        // Given an empty file
        File emptyFile = createEmptyFile();

        // When I try to upload it to the creditoAgricola endpoint
        Response response = given()
                .multiPart("file", emptyFile)
                .contentType("multipart/form-data")
                .when()
                .post(CreditoAgricolaApi.url);

        // Then it should fail
        int statusCode = response.statusCode();
        assertEquals(500, statusCode, "Should return error status code (500), got: " + statusCode);

        // Teardown
        assertTrue(emptyFile.delete(), "Fail to delete test file");
    }

    @Test
    void shouldReturnErrorWhenFileHasInvalidFormat() throws Exception {
        // Given an invalid format file
        File invalidFile = createCreditoAgricolaInvalidFile("this is not a number");

        // When I try to upload it
        Response response = given()
                .multiPart("file", invalidFile)
                .contentType("multipart/form-data")
                .when()
                .post(CreditoAgricolaApi.url);

        // Then it should fail
        int statusCode = response.statusCode();
        assertEquals(500, statusCode, "Should return error status code (500), got: " + statusCode);

        // Teardown
        assertTrue(invalidFile.delete(), "Fail to delete test file");
    }
}