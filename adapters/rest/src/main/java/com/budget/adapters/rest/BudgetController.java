package com.budget.adapters.rest;

import com.budget.core.BudgetService;
import com.budget.core.config.BankConfig;
import com.budget.core.config.BankConfigRequest;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@OpenAPIDefinition(info = @Info(title = "Budget API", version = "1.0"))
@RequestMapping("/budget")
public class BudgetController {
    @Autowired
    BudgetService budgetService;

    private ResponseEntity<ByteArrayResource> getBankFileResponse(Workbook workbook, String fileName) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        workbook.write(stream);
        workbook.close();

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=%s.xlsx", fileName));
        return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), header, HttpStatus.OK);
    }

    @PostMapping(value = "/file/all", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(description = "From all banks csv file, generates an Excel file with the correct format")
    @ApiResponse(responseCode = "200", description = "Excel file is generated successfully")
    public ResponseEntity<ByteArrayResource> allFilesToExcel(
            @RequestParam("activoBankFile") MultipartFile activoBankFile,
            @RequestParam("creditoAgricolaFile") MultipartFile creditoAgricolaFile,
            @RequestParam("cryptoComFile") MultipartFile cryptoComFile
    ) {
        try {
            Workbook workbook = budgetService.allFilesToExcel(activoBankFile, creditoAgricolaFile, cryptoComFile);
            return getBankFileResponse(workbook, "banks");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/file/activoBank", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(description = "From ActivoBank csv file, generates an Excel file with the correct format")
    @ApiResponse(responseCode = "200", description = "Excel file is generated successfully")
    public ResponseEntity<ByteArrayResource> activoBankFileToExcel(@RequestPart("file") MultipartFile file) {
        try {
            Workbook workbook = budgetService.activoBankFileToExcel(file);
            return getBankFileResponse(workbook, "activoBank");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/file/creditoAgricola", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(description = "From CreditoAgricola csv file, generates an Excel file with the correct format")
    @ApiResponse(responseCode = "200", description = "Excel file is generated successfully")
    public ResponseEntity<ByteArrayResource> creditoAgricolaFileToExcel(@RequestPart("file") MultipartFile file) {
        try {
            Workbook workbook = budgetService.creditoAgricolaFileToExcel(file);
            return getBankFileResponse(workbook, "creditoAgricola");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/file/cryptoCom", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(description = "From CryptoCom csv file, generates an Excel file with the correct format")
    @ApiResponse(responseCode = "200", description = "Excel file is generated successfully")
    public ResponseEntity<ByteArrayResource> cryptoComFileToExcel(@RequestPart("file") MultipartFile file) {
        try {
            Workbook workbook = budgetService.cryptoComFileToExcel(file);
            return getBankFileResponse(workbook, "cryptoCom");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/format/activoBank", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(description = "Returns the expected format for ActivoBank input files")
    @ApiResponse(responseCode = "200", description = "Format information retrieved successfully")
    public ResponseEntity<BankFileFormatDto> getActivoBankFormat() {
        try {
            BankConfig config = budgetService.getActivoBankConfig();
            BankFileFormatDto format = new BankFileFormatDto(
                    config.getName(),
                    "XLSX",
                    config.getFirstLine(),
                    config.getDateColumnPosition() + 1,
                    config.getAmountColumnPosition() + 1,
                    config.getDescriptionColumnPosition() + 1,
                    null,
                    config.getDateFormat(),
                    null,
                    config.createXlsxExample(config));
            return ResponseEntity.ok(format);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/format/creditoAgricola", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(description = "Returns the expected format for CreditoAgricola input files")
    @ApiResponse(responseCode = "200", description = "Format information retrieved successfully")
    public ResponseEntity<BankFileFormatDto> getCreditoAgricolaFormat() {
        try {
            BankConfig config = budgetService.getCreditoAgricolaConfig();
            BankFileFormatDto format = new BankFileFormatDto(
                    config.getName(),
                    "XLSX",
                    config.getFirstLine(),
                    config.getDateColumnPosition() + 1,
                    config.getAmountColumnPosition() + 1,
                    config.getDescriptionColumnPosition() + 1,
                    config.getCdColumnPosition() + 1,
                    config.getDateFormat(),
                    null,
                    config.createXlsxExample(config));
            return ResponseEntity.ok(format);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/format/cryptoCom", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(description = "Returns the expected format for CryptoCom input files")
    @ApiResponse(responseCode = "200", description = "Format information retrieved successfully")
    public ResponseEntity<BankFileFormatDto> getCryptoComFormat() {
        try {
            BankConfig config = budgetService.getCryptoComConfig();
            BankFileFormatDto format = new BankFileFormatDto(
                    config.getName(),
                    "CSV",
                    config.getFirstLine(),
                    config.getDateColumnPosition() + 1,
                    config.getAmountColumnPosition() + 1,
                    config.getDescriptionColumnPosition() + 1,
                    null,
                    config.getDateFormat(),
                    config.getDelimiter(),
                    config.createCsvExample(config));
            return ResponseEntity.ok(format);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/config/{bankName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update the configuration for a specific bank at runtime. Bank names: activoBank, creditoAgricola, cryptoCom")
    @ApiResponse(responseCode = "200", description = "Configuration updated successfully")
    @ApiResponse(responseCode = "400", description = "Unknown bank name")
    public ResponseEntity<Void> updateConfig(
            @PathVariable String bankName,
            @RequestBody BankConfigRequest request) {
        try {
            budgetService.updateConfig(bankName, request);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}