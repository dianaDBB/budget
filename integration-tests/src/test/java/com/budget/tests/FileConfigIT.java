package com.budget.tests;

import com.budget.BaseIT;
import com.budget.core.dto.GetBankFileFormatResponseDto;
import com.budget.apis.FileConfigApi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileConfigIT extends BaseIT {
    @Test
    void shouldReturnCreditoAgricolaFileFormat() {
        GetBankFileFormatResponseDto format = FileConfigApi.getConfig("CreditoAgricola");

        assertNotNull(format);
        assertEquals("CreditoAgricola", format.getBankName(), "'bankName' should be 'CreditoAgricola'");
        assertEquals("XLSX", format.getFileFormat(), "'fileFormat' should be 'XLSX'");
        assertEquals(6, format.getFirstDataLine(), "'firstDataLine' should be '6'");
        assertEquals(1, format.getDateColumnPosition(), "'dateColumnPosition' should be '1'");
        assertEquals(5, format.getAmountColumnPosition(), "'amountColumnPosition' should be '5'");
        assertEquals(3, format.getDescriptionColumnPosition(), "'descriptionColumnPosition' should be '3'");
        assertEquals(6, format.getCreditDebitColumnPosition(), "'creditDebitColumnPosition' should be '6'");
        assertEquals("dd/MM/yyyy", format.getDateFormat(), "'dateFormat' should be 'dd/MM/yyyy'");
        assertNull(format.getDelimiter(), "'delimiter' should be 'null'");

        assertNotNull(format.getHtmlExample(), "'htmlExample' should be not 'null'");
        assertTrue(format.getHtmlExample().contains("<table>"));
        assertTrue(format.getHtmlExample().contains("</table>"));
    }

    @Test
    void shouldReturnCryptoComFileFormat() {
        GetBankFileFormatResponseDto format = FileConfigApi.getConfig("CryptoCom");

        assertNotNull(format);
        assertEquals("CryptoCom", format.getBankName(), "'bankName' should be 'CryptoCom'");
        assertEquals("CSV", format.getFileFormat(), "'fileFormat' should be 'CSV'");
        assertEquals(2, format.getFirstDataLine(), "'firstDataLine' should be '2'");
        assertEquals(1, format.getDateColumnPosition(), "'dateColumnPosition' should be '1'");
        assertEquals(4, format.getAmountColumnPosition(), "'amountColumnPosition' should be '4'");
        assertEquals(2, format.getDescriptionColumnPosition(), "'descriptionColumnPosition' should be '2'");
        assertNull(format.getCreditDebitColumnPosition(), "'creditDebitColumnPosition' should be 'null'");
        assertEquals("yyyy-MM-dd hh:mm:ss", format.getDateFormat(), "'dateFormat' should be 'yyyy-MM-dd hh:mm:ss'");
        assertEquals(",", format.getDelimiter(), "'delimiter' should be ','");

        assertNotNull(format.getHtmlExample(), "'htmlExample' should be not 'null'");
        assertTrue(format.getHtmlExample().contains("<table>"));
        assertTrue(format.getHtmlExample().contains("</table>"));
    }

    @Test
    void shouldReturnActivoBankFileFormatViaApi() {
        GetBankFileFormatResponseDto format = FileConfigApi.getConfig("ActivoBank");

        assertNotNull(format);
        assertEquals("ActivoBank", format.getBankName(), "'bankName' should be 'ActivoBank'");
        assertEquals("XLSX", format.getFileFormat(), "'fileFormat' should be 'XLSX'");
        assertEquals(8, format.getFirstDataLine(), "'firstDataLine' should be '8'");
        assertEquals(1, format.getDateColumnPosition(), "'dateColumnPosition' should be '1'");
        assertEquals(4, format.getAmountColumnPosition(), "'amountColumnPosition' should be '4'");
        assertEquals(3, format.getDescriptionColumnPosition(), "'descriptionColumnPosition' should be '3'");
        assertNull(format.getCreditDebitColumnPosition(), "'creditDebitColumnPosition' should be 'null'");
        assertEquals("dd-MMMM-yyyy", format.getDateFormat(), "'dateFormat' should be 'dd-MMMM-yyyy'");
        assertNull(format.getDelimiter(), "'delimiter' should be 'null'");

        assertNotNull(format.getHtmlExample(), "'htmlExample' should be not 'null'");
        assertTrue(format.getHtmlExample().contains("<table>"));
        assertTrue(format.getHtmlExample().contains("</table>"));
    }
}
