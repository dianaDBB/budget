package com.budget.adapters.rest;

import com.budget.core.BudgetService;
import com.budget.core.dto.FileLineDto;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@OpenAPIDefinition(info = @Info(title = "Budget API", version = "1.0"))
@RequestMapping("/file")
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

    @PostMapping(value = "/all", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(description = "From all banks files, generates an Excel file with the correct format")
    @ApiResponse(responseCode = "200", description = "Excel file is generated successfully")
    public ResponseEntity<ByteArrayResource> allFilesToExcel(@RequestParam List<String> bankNames,
                                                             @RequestParam List<MultipartFile> files) {
        if (bankNames.size() != files.size()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            Workbook workbook = budgetService.allFilesToExcel(bankNames, files);
            return getBankFileResponse(workbook, "banks");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/preview-all", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(description = "From all banks files, previews the Excel file with the correct format")
    @ApiResponse(responseCode = "200", description = "Excel file is generated successfully")
    public ResponseEntity<List<FileLineDto>> previewAllFilesToExcel(@RequestParam List<String> bankNames,
                                                                    @RequestParam List<MultipartFile> files) {
        if (bankNames.size() != files.size()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        try {
            List<FileLineDto> preview = budgetService.previewAllFilesToExcel(bankNames, files);
            return new ResponseEntity<>(preview, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/preview-to-excel", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "From a preview of bank files, generates an Excel file with the correct format")
    @ApiResponse(responseCode = "200", description = "Excel file is generated successfully")
    public ResponseEntity<ByteArrayResource> previewAllFilesToExcelFile(@RequestBody List<FileLineDto> fileLines) {
        try {
            Workbook workbook = budgetService.fileLinesToExcel(fileLines);
            return getBankFileResponse(workbook, "preview");
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}