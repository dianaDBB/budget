package com.budget.tests;

import com.budget.BaseIT;
import com.budget.adapters.rest.dto.BankFileFormatDto;
import com.budget.apis.FileConfigApi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileConfigIT extends BaseIT {
    @Test
    void shouldReturnCreditoAgricolaFileFormat() {
        BankFileFormatDto format = FileConfigApi.getConfig("CreditoAgricola");

        assertNotNull(format);
        assertEquals("CreditoAgricola", format.bankName(), "'bankName' should be 'CreditoAgricola'");
        assertEquals("XLSX", format.fileFormat(), "'fileFormat' should be 'XLSX'");
        assertEquals(6, format.firstDataLine(), "'firstDataLine' should be '6'");
        assertEquals(1, format.dateColumnPosition(), "'dateColumnPosition' should be '1'");
        assertEquals(5, format.amountColumnPosition(), "'amountColumnPosition' should be '5'");
        assertEquals(3, format.descriptionColumnPosition(), "'descriptionColumnPosition' should be '3'");
        assertEquals(6, format.creditDebitColumnPosition(), "'creditDebitColumnPosition' should be '6'");
        assertEquals("dd/MM/yyyy", format.dateFormat(), "'dateFormat' should be 'dd/MM/yyyy'");
        assertNull(format.delimiter(), "'delimiter' should be 'null'");

        assertNotNull(format.htmlExample(), "'htmlExample' should be not 'null'");
        assertTrue(format.htmlExample().contains("<table>"));
        assertTrue(format.htmlExample().contains("</table>"));
    }

    @Test
    void shouldReturnCryptoComFileFormat() {
        BankFileFormatDto format = FileConfigApi.getConfig("CryptoCom");

        assertNotNull(format);
        assertEquals("CryptoCom", format.bankName(), "'bankName' should be 'CryptoCom'");
        assertEquals("CSV", format.fileFormat(), "'fileFormat' should be 'CSV'");
        assertEquals(2, format.firstDataLine(), "'firstDataLine' should be '2'");
        assertEquals(1, format.dateColumnPosition(), "'dateColumnPosition' should be '1'");
        assertEquals(4, format.amountColumnPosition(), "'amountColumnPosition' should be '4'");
        assertEquals(2, format.descriptionColumnPosition(), "'descriptionColumnPosition' should be '2'");
        assertNull(format.creditDebitColumnPosition(), "'creditDebitColumnPosition' should be 'null'");
        assertEquals("yyyy-MM-dd hh:mm:ss", format.dateFormat(), "'dateFormat' should be 'yyyy-MM-dd hh:mm:ss'");
        assertEquals(",", format.delimiter(), "'delimiter' should be ','");

        assertNotNull(format.htmlExample(), "'htmlExample' should be not 'null'");
        assertTrue(format.htmlExample().contains("<table>"));
        assertTrue(format.htmlExample().contains("</table>"));
    }

    @Test
    void shouldReturnActivoBankFileFormatViaApi() {
        BankFileFormatDto format = FileConfigApi.getConfig("ActivoBank");

        assertNotNull(format);
        assertEquals("ActivoBank", format.bankName(), "'bankName' should be 'ActivoBank'");
        assertEquals("XLSX", format.fileFormat(), "'fileFormat' should be 'XLSX'");
        assertEquals(8, format.firstDataLine(), "'firstDataLine' should be '8'");
        assertEquals(1, format.dateColumnPosition(), "'dateColumnPosition' should be '1'");
        assertEquals(4, format.amountColumnPosition(), "'amountColumnPosition' should be '4'");
        assertEquals(3, format.descriptionColumnPosition(), "'descriptionColumnPosition' should be '3'");
        assertNull(format.creditDebitColumnPosition(), "'creditDebitColumnPosition' should be 'null'");
        assertEquals("dd-MMMM-yyyy", format.dateFormat(), "'dateFormat' should be 'dd-MMMM-yyyy'");
        assertNull(format.delimiter(), "'delimiter' should be 'null'");

        assertNotNull(format.htmlExample(), "'htmlExample' should be not 'null'");
        assertTrue(format.htmlExample().contains("<table>"));
        assertTrue(format.htmlExample().contains("</table>"));
    }
}
