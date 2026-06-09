package com.budget.tests;

import com.budget.BaseIT;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class ActivoBankFileIT extends BaseIT {
    private File createActivoBankInputFile(String description, double amount) throws IOException {
        File file = new File("target/test-activoBank-" + System.currentTimeMillis() + ".xlsx");
        double initialBalance = 1000.00;
        LocalDate transDate = LocalDate.parse("01-May-2026", DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale.ENGLISH));
        LocalDate valueDate = LocalDate.parse("02-May-2026", DateTimeFormatter.ofPattern("dd-MMMM-yyyy", Locale.ENGLISH));

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Transactions");

            // Description header
            Row desc1 = sheet.createRow(0);
            setCell(desc1, 0, "HISTÓRICO DE CONTA NÚMERO 45602657434");

            Row desc2 = sheet.createRow(1);
            setCell(desc2, 0, "Moeda: EUR");

            Row desc3 = sheet.createRow(2);
            setCell(desc3, 0, "");

            Row desc4 = sheet.createRow(3);
            setCell(desc4, 0, "Tipo: Todos");

            Row desc5 = sheet.createRow(4);
            setCell(desc5, 0, "Data de: 01/05/2026");

            Row desc6 = sheet.createRow(5);
            setCell(desc6, 0, "Data até: 31/05/2026");

            Row desc7 = sheet.createRow(6);
            setCell(desc7, 0, "");

            // Header
            Row header = sheet.createRow(7);
            setCell(header, 0, "Data Lanc.");
            setCell(header, 1, "Data Valor");
            setCell(header, 2, "Descrição");
            setCell(header, 3, "Valor");
            setCell(header, 4, "Saldo");

            // Data row
            Row row = sheet.createRow(8);
            setCell(row, 0, transDate, "dd-MMMM-yyyy");
            setCell(row, 1, valueDate, "dd-MMMM-yyyy");
            setCell(row, 2, description);
            setCell(row, 3, amount);
            setCell(row, 4, initialBalance - amount);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }

        return file;
    }

    @Test
    void shouldUploadFileAndReturnValidXlsx() throws Exception {
        // Given I have an input file with a test transaction
        File inputFile = createActivoBankInputFile("Test Transaction", -50.00);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

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
        File inputFile = createActivoBankInputFile("COMPRA CONTINENTE Store", -45.50);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Home", getCellStringValue(workbook, 1, 3));
        assertEquals("Groceries", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeDiningOutTransaction() throws Exception {
        // Given I have an input file with a dining transaction
        File inputFile = createActivoBankInputFile("UBER    EATS Restaurant", -22.30);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Daily_Livings", getCellStringValue(workbook, 1, 3));
        assertEquals("Dining Out", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeStreamingTransaction() throws Exception {
        // Given I have an input file with a streaming subscription transaction
        File inputFile = createActivoBankInputFile("NETFLIX Subscription", -12.99);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Daily_Livings", getCellStringValue(workbook, 1, 3));
        assertEquals("Streaming", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeHealthTransaction() throws Exception {
        // Given I have an input file with a pharmacy transaction
        File inputFile = createActivoBankInputFile("FARMACIA Pharmacy", -18.50);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Daily_Livings", getCellStringValue(workbook, 1, 3));
        assertEquals("Health", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeGymTransaction() throws Exception {
        // Given I have an input file with a gym transaction
        File inputFile = createActivoBankInputFile("BALTAREJO Gym", -49.99);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Daily_Livings", getCellStringValue(workbook, 1, 3));
        assertEquals("Gym", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeInternetTransaction() throws Exception {
        // Given I have an input file with an internet transaction
        File inputFile = createActivoBankInputFile("VODAFONE Internet", -35.99);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Utilities", getCellStringValue(workbook, 1, 3));
        assertEquals("Internet", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizePhoneTransaction() throws Exception {
        // Given I have an input file with a phone transaction
        File inputFile = createActivoBankInputFile("NOS COM Phone", -40.50);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Utilities", getCellStringValue(workbook, 1, 3));
        assertEquals("Phone", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeFuelTransaction() throws Exception {
        // Given I have an input file with a fuel transaction
        File inputFile = createActivoBankInputFile("GALP Fuel Station", -60.00);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Car", getCellStringValue(workbook, 1, 3));
        assertEquals("Fuel", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeTollsTransaction() throws Exception {
        // Given I have an input file with a tolls transaction
        File inputFile = createActivoBankInputFile("VIA VERDE Toll", -8.50);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Car", getCellStringValue(workbook, 1, 3));
        assertEquals("Tolls", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeContractorTransaction() throws Exception {
        // Given I have an input file with a contractor transaction
        File inputFile = createActivoBankInputFile("IDEIAS DECIMAIS Contractor", -150.00);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("House_Construction", getCellStringValue(workbook, 1, 3));
        assertEquals("Contractor", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeSuppliesTransaction() throws Exception {
        // Given I have an input file with an office supplies transaction
        File inputFile = createActivoBankInputFile("COMPRA STAPLES Supplies", -25.75);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Job´s", getCellStringValue(workbook, 1, 3));
        assertEquals("Supplies", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeFeesTransaction() throws Exception {
        // Given I have an input file with a bank fees transaction
        File inputFile = createActivoBankInputFile("COMISSÃO S/ Bank Fee", -2.50);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Bank", getCellStringValue(workbook, 1, 3));
        assertEquals("Fees", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldCategorizeLoanTransaction() throws Exception {
        // Given I have an input file with a loan transaction
        File inputFile = createActivoBankInputFile("TRF.     0000351 00938121242", -250.00);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file has the correct Category / Sub-Category
        assertEquals("Home", getCellStringValue(workbook, 1, 3));
        assertEquals("Loan", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldNotCategorizeUnknownTransaction() throws Exception {
        // Given I have an input file with an unknown transaction
        File inputFile = createActivoBankInputFile("Unknown Random Purchase", -99.99);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file has empty Category / Sub-Category
        assertEquals("", getCellStringValue(workbook, 1, 3));
        assertEquals("", getCellStringValue(workbook, 1, 4));

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }

    @Test
    void shouldClassifyIncomeTransaction() throws Exception {
        // Given I have an input file with an income transaction
        File inputFile = createActivoBankInputFile("EUR Deposit Income", 1000.00);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

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
        File inputFile = createActivoBankInputFile("Random Purchase Expense", -75.50);

        // When I generate the budget file
        Workbook workbook = uploadActivoBankFile(inputFile);

        // Then the generated file classifies it as Expense with negative amount
        String type = getCellStringValue(workbook, 1, 2);
        double amount = getCellNumericValue(workbook, 1, 5);

        assertEquals("Expense", type, "Type should be Expense");
        assertTrue(amount <= 0, "Amount should be negative for expense");

        // Teardown
        assertTrue(inputFile.delete(), "Fail to delete the input file");
    }
}