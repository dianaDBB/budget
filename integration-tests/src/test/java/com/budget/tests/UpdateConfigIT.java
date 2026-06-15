package com.budget.tests;

import com.budget.BaseIT;
import com.budget.adapters.rest.BankFileFormatDto;
import com.budget.apis.ActivoBankApi;
import com.budget.apis.BankConfigApi;
import com.budget.apis.CreditoAgricolaApi;
import com.budget.apis.CryptoComApi;
import com.budget.apis.EntryDto;
import com.budget.core.config.BankConfigRequest;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static com.budget.helpers.FileXLSX.getCellStringValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateConfigIT extends BaseIT {

    @AfterEach
    void resetActivoBankConfig() {
        BankConfigRequest reset = new BankConfigRequest();
        reset.setFirstLine(8);
        reset.setDateFormat("dd-MMMM-yyyy");
        reset.setAmountColumnPosition(3);
        reset.setDateColumnPosition(0);
        reset.setDescriptionColumnPosition(2);
        reset.setCdColumnPosition(-1);
        BankConfigApi.updateConfig("activoBank", reset);
    }

    @AfterEach
    void resetCreditoAgricolaConfig() {
        BankConfigRequest reset = new BankConfigRequest();
        reset.setFirstLine(6);
        reset.setDateFormat("dd/MM/yyyy");
        reset.setAmountColumnPosition(4);
        reset.setDateColumnPosition(0);
        reset.setDescriptionColumnPosition(2);
        reset.setCdColumnPosition(5);
        BankConfigApi.updateConfig("creditoAgricola", reset);
    }

    @AfterEach
    void resetCryptoComConfig() {
        BankConfigRequest reset = new BankConfigRequest();
        reset.setFirstLine(2);
        reset.setDateFormat("yyyy-MM-dd hh:mm:ss");
        reset.setAmountColumnPosition(3);
        reset.setDateColumnPosition(0);
        reset.setDescriptionColumnPosition(1);
        reset.setCdColumnPosition(-1);
        reset.setDelimiter(",");
        BankConfigApi.updateConfig("cryptoCom", reset);
    }

    // --- activoBank ---

    @Test
    void shouldUpdateActivoBankFirstLine() {
        BankConfigRequest request = new BankConfigRequest();
        request.setFirstLine(10);

        Response response = BankConfigApi.updateConfig("activoBank", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = ActivoBankApi.getBankFormat();
        assertEquals(10, format.firstDataLine());
    }

    @Test
    void shouldUpdateActivoBankDateFormat() {
        BankConfigRequest request = new BankConfigRequest();
        request.setDateFormat("yyyy-MM-dd");

        Response response = BankConfigApi.updateConfig("activoBank", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = ActivoBankApi.getBankFormat();
        assertEquals("yyyy-MM-dd", format.dateFormat());
    }

    @Test
    void shouldUpdateActivoBankAmountColumnPosition() {
        BankConfigRequest request = new BankConfigRequest();
        request.setAmountColumnPosition(4);

        Response response = BankConfigApi.updateConfig("activoBank", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = ActivoBankApi.getBankFormat();
        assertEquals(5, format.amountColumnPosition()); // 1-indexed
    }

    @Test
    void shouldApplyPartialUpdateToActivoBank() {
        BankConfigRequest request = new BankConfigRequest();
        request.setFirstLine(9);
        request.setDescriptionColumnPosition(1);

        Response response = BankConfigApi.updateConfig("activoBank", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = ActivoBankApi.getBankFormat();
        assertEquals(9, format.firstDataLine());
        assertEquals(2, format.descriptionColumnPosition()); // 1-indexed
        // unchanged fields stay the same
        assertEquals("dd-MMMM-yyyy", format.dateFormat());
    }

    // --- creditoAgricola ---

    @Test
    void shouldUpdateCreditoAgricolaFirstLine() {
        BankConfigRequest request = new BankConfigRequest();
        request.setFirstLine(8);

        Response response = BankConfigApi.updateConfig("creditoAgricola", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = CreditoAgricolaApi.getFormat();
        assertEquals(8, format.firstDataLine());
    }

    @Test
    void shouldUpdateCreditoAgricolaDateFormat() {
        BankConfigRequest request = new BankConfigRequest();
        request.setDateFormat("MM/dd/yyyy");

        Response response = BankConfigApi.updateConfig("creditoAgricola", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = CreditoAgricolaApi.getFormat();
        assertEquals("MM/dd/yyyy", format.dateFormat());
    }

    // --- cryptoCom ---

    @Test
    void shouldUpdateCryptoComDelimiter() {
        BankConfigRequest request = new BankConfigRequest();
        request.setDelimiter(";");

        Response response = BankConfigApi.updateConfig("cryptoCom", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = CryptoComApi.getFormat();
        assertEquals(";", format.delimiter());
    }

    @Test
    void shouldUpdateCryptoComDateFormat() {
        BankConfigRequest request = new BankConfigRequest();
        request.setDateFormat("dd-MM-yyyy HH:mm:ss");

        Response response = BankConfigApi.updateConfig("cryptoCom", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = CryptoComApi.getFormat();
        assertEquals("dd-MM-yyyy HH:mm:ss", format.dateFormat());
    }

    // --- unknown bank ---

    @Test
    void shouldReturnBadRequestForUnknownBankName() {
        BankConfigRequest request = new BankConfigRequest();
        request.setFirstLine(5);

        Response response = BankConfigApi.updateConfig("unknownBank", request);

        assertEquals(400, response.statusCode());
    }

    // --- config change affects file processing ---

    @Test
    void shouldProcessFileWithNewConfigAndRejectFileWithOldConfig() throws Exception {
        // The default ActivoBank config has firstLine=8 (data starts at row index 8).
        // We update it so data starts at row index 5.
        int newFirstLine = 5;

        BankConfigRequest request = new BankConfigRequest();
        request.setFirstLine(newFirstLine);
        assertEquals(200, BankConfigApi.updateConfig("activoBank", request).statusCode());

        // --- upload a file built for the NEW config (data at row 5) → should succeed ---
        File newFormatFile = ActivoBankApi.createFileWithFirstLineAt(newFirstLine,
                List.of(new EntryDto("COMPRA CONTINENTE New Format", -30.00)));

        Workbook workbook = ActivoBankApi.generateFile(newFormatFile);

        Sheet sheet = workbook.getSheetAt(0);
        assertNotNull(sheet, "Workbook should have at least one sheet");
        assertTrue(sheet.getLastRowNum() >= 1, "Workbook should have at least one data row");
        assertEquals("Home", getCellStringValue(workbook, 1, 3));
        assertEquals("Groceries", getCellStringValue(workbook, 1, 4));

        assertTrue(newFormatFile.delete(), "Fail to delete new-format test file");

        // --- upload a file built for the OLD config (data at row 8) → should fail ---
        // With firstLine=5 the parser reads rows starting at index 5. In the original
        // file those rows contain header labels, which cannot be parsed as dates → 500.
        File oldFormatFile = ActivoBankApi.createValidFile(
                List.of(new EntryDto("COMPRA CONTINENTE Old Format", -30.00)));

        Response errorResponse = ActivoBankApi.generateFileRaw(oldFormatFile);
        assertEquals(500, errorResponse.statusCode(),
                "Uploading an old-format file after config change should return 500");

        assertTrue(oldFormatFile.delete(), "Fail to delete old-format test file");
    }
}
