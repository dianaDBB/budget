package com.budget.tests;

import com.budget.BaseIT;
import com.budget.apis.*;
import com.budget.dto.EntryDto;
import com.budget.helpers.ActivoBank;
import com.budget.helpers.CreditoAgricola;
import com.budget.helpers.CryptoCom;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static com.budget.helpers.FileXLSX.getCellNumericValue;
import static com.budget.helpers.FileXLSX.getCellStringValue;
import static org.junit.jupiter.api.Assertions.*;

public class AllFilesIT extends BaseIT {
    @Test
    void shouldUploadAllFilesAndReturnValidXlsx() throws Exception {
        File activoBankFile = ActivoBank.createValidFile(List.of(new EntryDto("Test Transaction ActivoBank", -50.00)));
        File creditoAgricolaFile = CreditoAgricola.createValidFile(List.of(new EntryDto("Test Transaction " +
                "CreditoAgricola", 30.00, "D")));
        File cryptoComFile = CryptoCom.createValidFile(List.of(new EntryDto("Test Transaction CryptoCom", -20.00)));

        Workbook workbook = BudgetApi.generateAllBanksFile(List.of("ActivoBank", "CreditoAgricola", "CryptoCom"),
                List.of(activoBankFile, creditoAgricolaFile, cryptoComFile));

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
        assertEquals(3, lastRow, "Should have exactly 3 data rows");

        for (int i = 1; i <= 3; i++) {
            Row row = sheet.getRow(i);
            assertNotNull(row, "Row " + i + " should exist");
            assertNotNull(row.getCell(0), "Row " + i + " should have Origin");
            assertNotNull(row.getCell(2), "Row " + i + " should have Type");
            assertNotNull(row.getCell(5), "Row " + i + " should have Value");
        }

        assertTrue(activoBankFile.delete(), "Fail to delete ActivoBank file");
        assertTrue(creditoAgricolaFile.delete(), "Fail to delete CreditoAgricola file");
        assertTrue(cryptoComFile.delete(), "Fail to delete CryptoCom file");
    }

    @Test
    void shouldCombineTransactionsFromAllBanks() throws Exception {
        File activoBankFile = ActivoBank.createValidFile(List.of(new EntryDto("COMPRA CONTINENTE Store", -45.50)));
        File creditoAgricolaFile = CreditoAgricola.createValidFile(List.of(new EntryDto("UBER    EATS Restaurant",
                22.30, "D")));
        File cryptoComFile = CryptoCom.createValidFile(List.of(new EntryDto("Bitcoin Purchase", -100.00)));

        Workbook workbook = BudgetApi.generateAllBanksFile(List.of("ActivoBank", "CreditoAgricola", "CryptoCom"),
                List.of(activoBankFile, creditoAgricolaFile, cryptoComFile));

        Sheet sheet = workbook.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        assertEquals(3, lastRow, "Should have exactly 3 data rows (one from each bank)");

        String origin1 = getCellStringValue(workbook, 1, 0);
        assertEquals("ActivoBank", origin1, "Row 1 should be from ActivoBank");
        double value1 = getCellNumericValue(workbook, 1, 5);
        assertEquals(-45.50, value1, "Row 1 should have value -45.50");

        String origin2 = getCellStringValue(workbook, 2, 0);
        assertEquals("CreditoAgricola", origin2, "Row 2 should be from CreditoAgricola");
        double value2 = getCellNumericValue(workbook, 2, 5);
        assertEquals(-22.30, value2, "Row 2 should have value -22.30");

        String origin3 = getCellStringValue(workbook, 3, 0);
        assertEquals("CryptoCom", origin3, "Row 3 should be from CryptoCom");
        double value3 = getCellNumericValue(workbook, 3, 5);
        assertEquals(-100.00, value3, "Row 3 should have value -100.00");

        assertTrue(activoBankFile.delete(), "Fail to delete ActivoBank file");
        assertTrue(creditoAgricolaFile.delete(), "Fail to delete CreditoAgricola file");
        assertTrue(cryptoComFile.delete(), "Fail to delete CryptoCom file");
    }

    @Test
    void shouldHandleMultipleTransactionsPerBank() throws Exception {
        File activoBankFile = ActivoBank.createValidFile(List.of(new EntryDto("COMPRA CONTINENTE Store", -45.50),
                new EntryDto("NETFLIX Subscription", -12.99), new EntryDto("GALP Fuel Station", -60.00)));
        File creditoAgricolaFile = CreditoAgricola.createValidFile(List.of(new EntryDto("UBER    EATS Restaurant",
                22.30, "D"), new EntryDto("VODAFONE Internet", 35.99, "D")));
        File cryptoComFile = CryptoCom.createValidFile(List.of(new EntryDto("Bitcoin Purchase", -100.00),
                new EntryDto("Ethereum Purchase", -50.00)));

        Workbook workbook = BudgetApi.generateAllBanksFile(List.of("ActivoBank", "CreditoAgricola", "CryptoCom"),
                List.of(activoBankFile, creditoAgricolaFile, cryptoComFile));

        Sheet sheet = workbook.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        assertEquals(7, lastRow, "Should have exactly 7 data rows (3 + 2 + 2 transactions)");

        assertEquals("ActivoBank", getCellStringValue(workbook, 1, 0));
        assertEquals(-45.50, getCellNumericValue(workbook, 1, 5));

        assertEquals("ActivoBank", getCellStringValue(workbook, 2, 0));
        assertEquals(-12.99, getCellNumericValue(workbook, 2, 5));

        assertEquals("ActivoBank", getCellStringValue(workbook, 3, 0));
        assertEquals(-60.00, getCellNumericValue(workbook, 3, 5));

        assertEquals("CreditoAgricola", getCellStringValue(workbook, 4, 0));
        assertEquals(-22.30, getCellNumericValue(workbook, 4, 5));

        assertEquals("CreditoAgricola", getCellStringValue(workbook, 5, 0));
        assertEquals(-35.99, getCellNumericValue(workbook, 5, 5));

        assertEquals("CryptoCom", getCellStringValue(workbook, 6, 0));
        assertEquals(-100.00, getCellNumericValue(workbook, 6, 5));

        assertEquals("CryptoCom", getCellStringValue(workbook, 7, 0));
        assertEquals(-50.00, getCellNumericValue(workbook, 7, 5));

        assertTrue(activoBankFile.delete(), "Fail to delete ActivoBank file");
        assertTrue(creditoAgricolaFile.delete(), "Fail to delete CreditoAgricola file");
        assertTrue(cryptoComFile.delete(), "Fail to delete CryptoCom file");
    }

    @Test
    void shouldCategorizeTransactionsFromAllBanks() throws Exception {
        File activoBankFile = ActivoBank.createValidFile(List.of(new EntryDto("COMPRA CONTINENTE Store", -45.50)));
        File creditoAgricolaFile = CreditoAgricola.createValidFile(List.of(new EntryDto("UBER    EATS Restaurant",
                22.30, "D")));
        File cryptoComFile = CryptoCom.createValidFile(List.of(new EntryDto("Bitcoin Purchase", -100.00)));

        Workbook workbook = BudgetApi.generateAllBanksFile(List.of("ActivoBank", "CreditoAgricola", "CryptoCom"),
                List.of(activoBankFile, creditoAgricolaFile, cryptoComFile));

        Sheet sheet = workbook.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        assertEquals(3, lastRow, "Should have exactly 3 data rows");

        assertEquals("ActivoBank", getCellStringValue(workbook, 1, 0));
        assertEquals("Home", getCellStringValue(workbook, 1, 3));
        assertEquals("Groceries", getCellStringValue(workbook, 1, 4));
        assertEquals(-45.50, getCellNumericValue(workbook, 1, 5));

        assertEquals("CreditoAgricola", getCellStringValue(workbook, 2, 0));
        assertEquals("Daily_Livings", getCellStringValue(workbook, 2, 3));
        assertEquals("Dining Out", getCellStringValue(workbook, 2, 4));
        assertEquals(-22.30, getCellNumericValue(workbook, 2, 5));

        assertEquals("CryptoCom", getCellStringValue(workbook, 3, 0));
        assertEquals(-100.00, getCellNumericValue(workbook, 3, 5));

        assertTrue(activoBankFile.delete(), "Fail to delete ActivoBank file");
        assertTrue(creditoAgricolaFile.delete(), "Fail to delete CreditoAgricola file");
        assertTrue(cryptoComFile.delete(), "Fail to delete CryptoCom file");
    }

    @Test
    void shouldClassifyIncomeAndExpenseTransactionsFromAllBanks() throws Exception {
        File activoBankFile = ActivoBank.createValidFile(List.of(new EntryDto("EUR Deposit Income", 1000.00),
                new EntryDto("COMPRA CONTINENTE Store", -45.50)));
        File creditoAgricolaFile = CreditoAgricola.createValidFile(List.of(new EntryDto("UBER    EATS Restaurant",
                22.30, "D")));
        File cryptoComFile = CryptoCom.createValidFile(List.of(new EntryDto("Bitcoin Purchase", -100.00)));

        Workbook workbook = BudgetApi.generateAllBanksFile(List.of("ActivoBank", "CreditoAgricola", "CryptoCom"),
                List.of(activoBankFile, creditoAgricolaFile, cryptoComFile));

        Sheet sheet = workbook.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        assertEquals(4, lastRow, "Should have exactly 4 data rows (2 ActivoBank + 1 CreditoAgricola + 1 CryptoCom)");

        assertEquals("ActivoBank", getCellStringValue(workbook, 1, 0));
        String type1 = getCellStringValue(workbook, 1, 2);
        assertEquals("TransferInHouse", type1, "Row 1 should be classified as TransferInHouse");
        assertEquals(1000.00, getCellNumericValue(workbook, 1, 5));

        assertEquals("ActivoBank", getCellStringValue(workbook, 2, 0));
        assertEquals("Expense", getCellStringValue(workbook, 2, 2));
        assertEquals(-45.50, getCellNumericValue(workbook, 2, 5));

        assertEquals("CreditoAgricola", getCellStringValue(workbook, 3, 0));
        assertEquals("Expense", getCellStringValue(workbook, 3, 2));
        assertEquals(-22.30, getCellNumericValue(workbook, 3, 5));

        assertEquals("CryptoCom", getCellStringValue(workbook, 4, 0));
        assertEquals("Expense", getCellStringValue(workbook, 4, 2));
        assertEquals(-100.00, getCellNumericValue(workbook, 4, 5));

        assertTrue(activoBankFile.delete(), "Fail to delete ActivoBank file");
        assertTrue(creditoAgricolaFile.delete(), "Fail to delete CreditoAgricola file");
        assertTrue(cryptoComFile.delete(), "Fail to delete CryptoCom file");
    }

}


