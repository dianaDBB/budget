package com.budget.tests;

import com.budget.BaseIT;
import com.budget.apis.Entry;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static com.budget.apis.ActivoBankApi.createActivoBankFile;
import static com.budget.apis.AllBanksApi.uploadAllBankFiles;
import static com.budget.apis.CreditoAgricolaApi.createCreditoAgricolaFile;
import static com.budget.apis.CryptoComApi.createCryptoComFile;
import static com.budget.helpers.FileXLSX.getCellNumericValue;
import static com.budget.helpers.FileXLSX.getCellStringValue;
import static org.junit.jupiter.api.Assertions.*;

public class AllFilesIT extends BaseIT {
    @Test
    void shouldUploadAllFilesAndReturnValidXlsx() throws Exception {
        // Given I have input files from all three banks with one transaction each
        File activoBankFile = createActivoBankFile(List.of(new Entry("Test Transaction ActivoBank", -50.00)));
        File creditoAgricolaFile = createCreditoAgricolaFile(List.of(new Entry("Test Transaction CreditoAgricola", 30.00, "D")));
        File cryptoComFile = createCryptoComFile(List.of(new Entry("Test Transaction CryptoCom", -20.00)));

        // When I generate the combined budget file
        Workbook workbook = uploadAllBankFiles(activoBankFile, creditoAgricolaFile, cryptoComFile);

        // Then the file has exactly the correct structure
        Sheet sheet = workbook.getSheetAt(0);
        assertNotNull(sheet, "File should have at least 1 sheet");

        // Verify headers are correct
        Row header = sheet.getRow(0);
        assertNotNull(header);
        assertEquals("Origin", header.getCell(0).getStringCellValue());
        assertEquals("Date", header.getCell(1).getStringCellValue());
        assertEquals("Type", header.getCell(2).getStringCellValue());
        assertEquals("Category", header.getCell(3).getStringCellValue());
        assertEquals("Sub-category", header.getCell(4).getStringCellValue());
        assertEquals("Value", header.getCell(5).getStringCellValue());
        assertEquals("Original Description", header.getCell(6).getStringCellValue());

        // Exactly 3 data rows (one from each bank)
        int lastRow = sheet.getLastRowNum();
        assertEquals(3, lastRow, "Should have exactly 3 data rows");

        // Verify each row contains a transaction
        for (int i = 1; i <= 3; i++) {
            Row row = sheet.getRow(i);
            assertNotNull(row, "Row " + i + " should exist");
            assertNotNull(row.getCell(0), "Row " + i + " should have Origin");
            assertNotNull(row.getCell(2), "Row " + i + " should have Type");
            assertNotNull(row.getCell(5), "Row " + i + " should have Value");
        }

        // Teardown
        assertTrue(activoBankFile.delete(), "Fail to delete ActivoBank file");
        assertTrue(creditoAgricolaFile.delete(), "Fail to delete CreditoAgricola file");
        assertTrue(cryptoComFile.delete(), "Fail to delete CryptoCom file");
    }

    @Test
    void shouldCombineTransactionsFromAllBanks() throws Exception {
        // Given I have input files with one specific transaction from each bank
        File activoBankFile = createActivoBankFile(List.of(new Entry("COMPRA CONTINENTE Store", -45.50)));
        File creditoAgricolaFile = createCreditoAgricolaFile(List.of(new Entry("UBER    EATS Restaurant", 22.30, "D")));
        File cryptoComFile = createCryptoComFile(List.of(new Entry("Bitcoin Purchase", -100.00)));

        // When I generate the combined budget file
        Workbook workbook = uploadAllBankFiles(activoBankFile, creditoAgricolaFile, cryptoComFile);

        // Then the file contains exactly 3 data rows with transactions from all banks
        Sheet sheet = workbook.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        assertEquals(3, lastRow, "Should have exactly 3 data rows (one from each bank)");

        // Verify first transaction (ActivoBank with grocery purchase)
        String origin1 = getCellStringValue(workbook, 1, 0);
        assertEquals("ActivoBank", origin1, "Row 1 should be from ActivoBank");
        double value1 = getCellNumericValue(workbook, 1, 5);
        assertEquals(-45.50, value1, "Row 1 should have value -45.50");

        // Verify second transaction (CreditoAgricola with dining)
        String origin2 = getCellStringValue(workbook, 2, 0);
        assertEquals("CreditoAgricola", origin2, "Row 2 should be from CreditoAgricola");
        double value2 = getCellNumericValue(workbook, 2, 5);
        assertEquals(-22.30, value2, "Row 2 should have value -22.30");

        // Verify third transaction (CryptoCom with crypto purchase)
        String origin3 = getCellStringValue(workbook, 3, 0);
        assertEquals("CryptoCom", origin3, "Row 3 should be from CryptoCom");
        double value3 = getCellNumericValue(workbook, 3, 5);
        assertEquals(-100.00, value3, "Row 3 should have value -100.00");

        // Teardown
        assertTrue(activoBankFile.delete(), "Fail to delete ActivoBank file");
        assertTrue(creditoAgricolaFile.delete(), "Fail to delete CreditoAgricola file");
        assertTrue(cryptoComFile.delete(), "Fail to delete CryptoCom file");
    }

    @Test
    void shouldHandleMultipleTransactionsPerBank() throws Exception {
        // Given I have files with multiple transactions from each bank
        // ActivoBank: 3 transactions
        // CreditoAgricola: 2 transactions
        // CryptoCom: 2 transactions
        // Total: 7 transactions
        File activoBankFile = createActivoBankFile(List.of(
                new Entry("COMPRA CONTINENTE Store", -45.50),
                new Entry("NETFLIX Subscription", -12.99),
                new Entry("GALP Fuel Station", -60.00)
        ));
        File creditoAgricolaFile = createCreditoAgricolaFile(List.of(
                new Entry("UBER    EATS Restaurant", 22.30, "D"),
                new Entry("VODAFONE Internet", 35.99, "D")
        ));
        File cryptoComFile = createCryptoComFile(List.of(
                new Entry("Bitcoin Purchase", -100.00),
                new Entry("Ethereum Purchase", -50.00)
        ));

        // When I generate the combined budget file
        Workbook workbook = uploadAllBankFiles(activoBankFile, creditoAgricolaFile, cryptoComFile);

        // Then the file contains exactly 7 data rows
        Sheet sheet = workbook.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        assertEquals(7, lastRow, "Should have exactly 7 data rows (3 + 2 + 2 transactions)");

        // Verify ActivoBank transactions (rows 1-3)
        assertEquals("ActivoBank", getCellStringValue(workbook, 1, 0));
        assertEquals(-45.50, getCellNumericValue(workbook, 1, 5));

        assertEquals("ActivoBank", getCellStringValue(workbook, 2, 0));
        assertEquals(-12.99, getCellNumericValue(workbook, 2, 5));

        assertEquals("ActivoBank", getCellStringValue(workbook, 3, 0));
        assertEquals(-60.00, getCellNumericValue(workbook, 3, 5));

        // Verify CreditoAgricola transactions (rows 4-5)
        assertEquals("CreditoAgricola", getCellStringValue(workbook, 4, 0));
        assertEquals(-22.30, getCellNumericValue(workbook, 4, 5));

        assertEquals("CreditoAgricola", getCellStringValue(workbook, 5, 0));
        assertEquals(-35.99, getCellNumericValue(workbook, 5, 5));

        // Verify CryptoCom transactions (rows 6-7)
        assertEquals("CryptoCom", getCellStringValue(workbook, 6, 0));
        assertEquals(-100.00, getCellNumericValue(workbook, 6, 5));

        assertEquals("CryptoCom", getCellStringValue(workbook, 7, 0));
        assertEquals(-50.00, getCellNumericValue(workbook, 7, 5));

        // Teardown
        assertTrue(activoBankFile.delete(), "Fail to delete ActivoBank file");
        assertTrue(creditoAgricolaFile.delete(), "Fail to delete CreditoAgricola file");
        assertTrue(cryptoComFile.delete(), "Fail to delete CryptoCom file");
    }

    @Test
    void shouldCategorizeTransactionsFromAllBanks() throws Exception {
        // Given I have files with transactions that should be categorized
        File activoBankFile = createActivoBankFile(List.of(new Entry("COMPRA CONTINENTE Store", -45.50)));
        File creditoAgricolaFile = createCreditoAgricolaFile(List.of(new Entry("UBER    EATS Restaurant", 22.30, "D")));
        File cryptoComFile = createCryptoComFile(List.of(new Entry("Bitcoin Purchase", -100.00)));

        // When I generate the combined budget file
        Workbook workbook = uploadAllBankFiles(activoBankFile, creditoAgricolaFile, cryptoComFile);

        // Then the file has exactly 3 data rows with categorization applied
        Sheet sheet = workbook.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        assertEquals(3, lastRow, "Should have exactly 3 data rows");

        // Verify row 1: Grocery transaction should be categorized as Home/Groceries
        assertEquals("ActivoBank", getCellStringValue(workbook, 1, 0));
        assertEquals("Home", getCellStringValue(workbook, 1, 3));
        assertEquals("Groceries", getCellStringValue(workbook, 1, 4));
        assertEquals(-45.50, getCellNumericValue(workbook, 1, 5));

        // Verify row 2: Dining transaction should be categorized as Daily_Livings/Dining Out
        assertEquals("CreditoAgricola", getCellStringValue(workbook, 2, 0));
        assertEquals("Daily_Livings", getCellStringValue(workbook, 2, 3));
        assertEquals("Dining Out", getCellStringValue(workbook, 2, 4));
        assertEquals(-22.30, getCellNumericValue(workbook, 2, 5));

        // Verify row 3: Crypto transaction should have no category (not recognized)
        assertEquals("CryptoCom", getCellStringValue(workbook, 3, 0));
        assertEquals(-100.00, getCellNumericValue(workbook, 3, 5));

        // Teardown
        assertTrue(activoBankFile.delete(), "Fail to delete ActivoBank file");
        assertTrue(creditoAgricolaFile.delete(), "Fail to delete CreditoAgricola file");
        assertTrue(cryptoComFile.delete(), "Fail to delete CryptoCom file");
    }

    @Test
    void shouldClassifyIncomeAndExpenseTransactionsFromAllBanks() throws Exception {
        // Given I have files with both income and expense transactions
        File activoBankFile = createActivoBankFile(List.of(
                new Entry("EUR Deposit Income", 1000.00),
                new Entry("COMPRA CONTINENTE Store", -45.50)
        ));
        File creditoAgricolaFile = createCreditoAgricolaFile(List.of(
                new Entry("UBER    EATS Restaurant", 22.30, "D")
        ));
        File cryptoComFile = createCryptoComFile(List.of(
                new Entry("Bitcoin Purchase", -100.00)
        ));

        // When I generate the combined budget file
        Workbook workbook = uploadAllBankFiles(activoBankFile, creditoAgricolaFile, cryptoComFile);

        // Then the file has exactly 4 data rows with correct classification
        Sheet sheet = workbook.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        assertEquals(4, lastRow, "Should have exactly 4 data rows (2 ActivoBank + 1 CreditoAgricola + 1 CryptoCom)");

        // Verify row 1: Income transaction should be classified as Income with positive amount
        assertEquals("ActivoBank", getCellStringValue(workbook, 1, 0));
        String type1 = getCellStringValue(workbook, 1, 2);
        assertTrue("Income".equals(type1) || "TrasnferInHouse".equals(type1), "Row 1 should be classified as Income or Transfer");
        assertEquals(1000.00, getCellNumericValue(workbook, 1, 5));

        // Verify row 2: Expense transaction should be classified as Expense with negative amount
        assertEquals("ActivoBank", getCellStringValue(workbook, 2, 0));
        assertEquals("Expense", getCellStringValue(workbook, 2, 2));
        assertEquals(-45.50, getCellNumericValue(workbook, 2, 5));

        // Verify row 3: Dining expense
        assertEquals("CreditoAgricola", getCellStringValue(workbook, 3, 0));
        assertEquals("Expense", getCellStringValue(workbook, 3, 2));
        assertEquals(-22.30, getCellNumericValue(workbook, 3, 5));

        // Verify row 4: Crypto expense
        assertEquals("CryptoCom", getCellStringValue(workbook, 4, 0));
        assertEquals("Expense", getCellStringValue(workbook, 4, 2));
        assertEquals(-100.00, getCellNumericValue(workbook, 4, 5));

        // Teardown
        assertTrue(activoBankFile.delete(), "Fail to delete ActivoBank file");
        assertTrue(creditoAgricolaFile.delete(), "Fail to delete CreditoAgricola file");
        assertTrue(cryptoComFile.delete(), "Fail to delete CryptoCom file");
    }


}


