package com.budget.adapters.rest;

import com.budget.core.BudgetService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@OpenAPIDefinition(servers = @Server(url = "http://localhost:8443/budget", description = "Budget API"))
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

    @PostMapping(value = "/file/all", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(description = "From all banks csv file, generates an Excel file with the correct format")
    @ApiResponse(responseCode = "200", description = "Excel file is generated successfully")
    public ResponseEntity<ByteArrayResource> allFilesToExcel (
            @RequestPart("activoBankFile") MultipartFile activoBankFile,
            @RequestPart("creditoAgricolaFile") MultipartFile creditoAgricolaFile,
            @RequestPart("cryptoComFile") MultipartFile cryptoComFile
    ){
        try {
            Workbook workbook = budgetService.allFilesToExcel(
                    /*montepioFile,*/ activoBankFile, creditoAgricolaFile, cryptoComFile);
            return getBankFileResponse(workbook, "banks");
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/file/montepio", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(description = "From Montepio csv file, generates an Excel file with the correct format")
    @ApiResponse(responseCode = "200", description = "Excel file is generated successfully")
    public ResponseEntity<ByteArrayResource> montepioFileToExcel (@RequestPart("file") MultipartFile file) {
        try {
            Workbook workbook = budgetService.montepioFileToExcel(file);
            return getBankFileResponse(workbook, "montepio");
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/file/activoBank", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(description = "From ActivoBank csv file, generates an Excel file with the correct format")
    @ApiResponse(responseCode = "200", description = "Excel file is generated successfully")
    public ResponseEntity<ByteArrayResource> activoBankFileToExcel (@RequestPart("file") MultipartFile file) {
        try {
            Workbook workbook = budgetService.activoBankFileToExcel(file);
            return getBankFileResponse(workbook, "activoBank");
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/file/creditoAgricola", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(description = "From CreditoAgricola csv file, generates an Excel file with the correct format")
    @ApiResponse(responseCode = "200", description = "Excel file is generated successfully")
    public ResponseEntity<ByteArrayResource> creditoAgricolaFileToExcel (@RequestPart("file") MultipartFile file) {
        try {
            Workbook workbook = budgetService.creditoAgricolaFileToExcel(file);
            return getBankFileResponse(workbook, "creditoAgricola");
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/file/cryptoCom", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(description = "From CryptoCom csv file, generates an Excel file with the correct format")
    @ApiResponse(responseCode = "200", description = "Excel file is generated successfully")
    public ResponseEntity<ByteArrayResource> cryptoComFileToExcel (@RequestPart("file") MultipartFile file) {
        try {
            Workbook workbook = budgetService.cryptoComFileToExcel(file);
            return getBankFileResponse(workbook, "cryptoCom");
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}