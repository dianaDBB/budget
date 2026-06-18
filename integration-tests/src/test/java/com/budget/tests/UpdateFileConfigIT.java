package com.budget.tests;

import com.budget.BaseIT;
import com.budget.adapters.rest.dto.BankFileFormatDto;
import com.budget.core.dto.UpdateFileConfigRequestDto;
import com.budget.apis.BudgetApi;
import com.budget.apis.FileConfigApi;
import com.budget.dto.EntryDto;
import com.budget.helpers.ActivoBank;

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

public class UpdateFileConfigIT extends BaseIT {

    @AfterEach
    void resetActivoBankConfig() {
        UpdateFileConfigRequestDto reset = new UpdateFileConfigRequestDto();
        reset.setFirstLine(8);
        reset.setDateFormat("dd-MMMM-yyyy");
        reset.setAmountColumnPosition(3);
        reset.setDateColumnPosition(0);
        reset.setDescriptionColumnPosition(2);
        reset.setCdColumnPosition(-1);
        FileConfigApi.updateConfig("activoBank", reset);
    }

    @AfterEach
    void resetCreditoAgricolaConfig() {
        UpdateFileConfigRequestDto reset = new UpdateFileConfigRequestDto();
        reset.setFirstLine(6);
        reset.setDateFormat("dd/MM/yyyy");
        reset.setAmountColumnPosition(4);
        reset.setDateColumnPosition(0);
        reset.setDescriptionColumnPosition(2);
        reset.setCdColumnPosition(5);
        FileConfigApi.updateConfig("creditoAgricola", reset);
    }

    @AfterEach
    void resetCryptoComConfig() {
        UpdateFileConfigRequestDto reset = new UpdateFileConfigRequestDto();
        reset.setFirstLine(2);
        reset.setDateFormat("yyyy-MM-dd hh:mm:ss");
        reset.setAmountColumnPosition(3);
        reset.setDateColumnPosition(0);
        reset.setDescriptionColumnPosition(1);
        reset.setCdColumnPosition(-1);
        reset.setDelimiter(",");
        FileConfigApi.updateConfig("cryptoCom", reset);
    }

    // --- activoBank ---

    @Test
    void shouldUpdateActivoBankFirstLine() {
        UpdateFileConfigRequestDto request = new UpdateFileConfigRequestDto();
        request.setFirstLine(10);

        Response response = FileConfigApi.updateConfig("ActivoBank", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = FileConfigApi.getConfig("ActivoBank");
        assertEquals(10, format.firstDataLine());
    }

    @Test
    void shouldUpdateActivoBankDateFormat() {
        UpdateFileConfigRequestDto request = new UpdateFileConfigRequestDto();
        request.setDateFormat("yyyy-MM-dd");

        Response response = FileConfigApi.updateConfig("activoBank", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = FileConfigApi.getConfig("ActivoBank");
        assertEquals("yyyy-MM-dd", format.dateFormat());
    }

    @Test
    void shouldUpdateActivoBankAmountColumnPosition() {
        UpdateFileConfigRequestDto request = new UpdateFileConfigRequestDto();
        request.setAmountColumnPosition(4);

        Response response = FileConfigApi.updateConfig("ActivoBank", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = FileConfigApi.getConfig("ActivoBank");
        assertEquals(5, format.amountColumnPosition()); // 1-indexed
    }

    @Test
    void shouldApplyPartialUpdateToActivoBank() {
        UpdateFileConfigRequestDto request = new UpdateFileConfigRequestDto();
        request.setFirstLine(9);
        request.setDescriptionColumnPosition(1);

        Response response = FileConfigApi.updateConfig("ActivoBank", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = FileConfigApi.getConfig("ActivoBank");
        assertEquals(9, format.firstDataLine());
        assertEquals(2, format.descriptionColumnPosition()); // 1-indexed
        // unchanged fields stay the same
        assertEquals("dd-MMMM-yyyy", format.dateFormat());
    }

    // --- creditoAgricola ---

    @Test
    void shouldUpdateCreditoAgricolaFirstLine() {
        UpdateFileConfigRequestDto request = new UpdateFileConfigRequestDto();
        request.setFirstLine(8);

        Response response = FileConfigApi.updateConfig("CreditoAgricola", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = FileConfigApi.getConfig("CreditoAgricola");
        assertEquals(8, format.firstDataLine());
    }

    @Test
    void shouldUpdateCreditoAgricolaDateFormat() {
        UpdateFileConfigRequestDto request = new UpdateFileConfigRequestDto();
        request.setDateFormat("MM/dd/yyyy");

        Response response = FileConfigApi.updateConfig("CreditoAgricola", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = FileConfigApi.getConfig("CreditoAgricola");
        assertEquals("MM/dd/yyyy", format.dateFormat());
    }

    // --- cryptoCom ---

    @Test
    void shouldUpdateCryptoComDelimiter() {
        UpdateFileConfigRequestDto request = new UpdateFileConfigRequestDto();
        request.setDelimiter(";");

        Response response = FileConfigApi.updateConfig("CryptoCom", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = FileConfigApi.getConfig("CryptoCom");
        assertEquals(";", format.delimiter());
    }

    @Test
    void shouldUpdateCryptoComDateFormat() {
        UpdateFileConfigRequestDto request = new UpdateFileConfigRequestDto();
        request.setDateFormat("dd-MM-yyyy HH:mm:ss");

        Response response = FileConfigApi.updateConfig("CryptoCom", request);

        assertEquals(200, response.statusCode());

        BankFileFormatDto format = FileConfigApi.getConfig("CryptoCom");
        assertEquals("dd-MM-yyyy HH:mm:ss", format.dateFormat());
    }

    // --- unknown bank ---

    @Test
    void shouldReturnBadRequestForUnknownBankName() {
        UpdateFileConfigRequestDto request = new UpdateFileConfigRequestDto();
        request.setFirstLine(5);

        Response response = FileConfigApi.updateConfig("unknownBank", request);

        assertEquals(400, response.statusCode());
    }

    // --- config change affects file processing ---

    @Test
    void shouldProcessFileWithNewConfigAndRejectFileWithOldConfig() throws Exception {
        // The default ActivoBank config has firstLine=8 (data starts at row index 8).
        // We update it so data starts at row index 5.
        int newFirstLine = 5;

        UpdateFileConfigRequestDto request = new UpdateFileConfigRequestDto();
        request.setFirstLine(newFirstLine);
        assertEquals(200, FileConfigApi.updateConfig("ActivoBank", request).statusCode());

        // --- upload a file built for the NEW config (data at row 5) → should succeed ---
        File newFormatFile = ActivoBank.createFileWithFirstLineAt(newFirstLine,
                List.of(new EntryDto("COMPRA CONTINENTE New Format", -30.00)));

        Workbook workbook = BudgetApi.generateBankFile("ActivoBank", newFormatFile);

        Sheet sheet = workbook.getSheetAt(0);
        assertNotNull(sheet, "Workbook should have at least one sheet");
        assertTrue(sheet.getLastRowNum() >= 1, "Workbook should have at least one data row");
        assertEquals("Home", getCellStringValue(workbook, 1, 3));
        assertEquals("Groceries", getCellStringValue(workbook, 1, 4));

        assertTrue(newFormatFile.delete(), "Fail to delete new-format test file");

        // --- upload a file built for the OLD config (data at row 8) → should fail ---
        // With firstLine=5 the parser reads rows starting at index 5. In the original
        // file those rows contain header labels, which cannot be parsed as dates → 500.
        File oldFormatFile = ActivoBank.createValidFile(
                List.of(new EntryDto("COMPRA CONTINENTE Old Format", -30.00)));

        Response errorResponse = BudgetApi.generateBankFileRaw("ActivoBank", oldFormatFile);
        assertEquals(500, errorResponse.statusCode(),
                "Uploading an old-format file after config change should return 500");

        assertTrue(oldFormatFile.delete(), "Fail to delete old-format test file");
    }
}
