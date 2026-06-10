package com.budget.tests;

import com.budget.BaseIT;
import com.budget.adapters.rest.BankFileFormatDto;
import com.budget.apis.ActivoBankApi;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BankFormatIT extends BaseIT {
    @Test
    void shouldReturnActivoBankFileFormat() {
        BankFileFormatDto format = ActivoBankApi.getBankFormat();

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