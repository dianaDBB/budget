package com.budget.adapters.rest;

import com.budget.core.dto.AddFileConfigRequestDto;
import com.budget.core.dto.GetBankFileFormatResponseDto;
import com.budget.core.dto.UpdateFileConfigRequestDto;
import com.budget.core.FileConfigService;
import com.budget.core.entity.FileConfigEntity;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@OpenAPIDefinition(info = @Info(title = "Budget API", version = "1.0"))
@RequestMapping("/file-config")
public class FileConfigController {
    @Autowired
    FileConfigService fileConfigService;

    @GetMapping(value = "/{bankName}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(description = "Returns the expected format for the specified bank input files")
    @ApiResponse(responseCode = "200", description = "Format information retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Unknown bank name")
    public ResponseEntity<GetBankFileFormatResponseDto> getBankFileFormat(@PathVariable String bankName) {
        FileConfigEntity config = fileConfigService.getFileConfig(bankName);

        boolean isCsv = "CSV".equals(config.getFileFormat());
        String htmlExample = isCsv ? config.createCsvExample() : config.createXlsxExample();
        GetBankFileFormatResponseDto format = new GetBankFileFormatResponseDto(config.getBankName(), isCsv ? "CSV" :
                "XLSX", config.getFirstLine(), config.getDateColumnPos(), config.getAmountColumnPos(),
                config.getDescColumnPos(), config.getCreditDebitColumnPos() == -1 ? null :
                config.getCreditDebitColumnPos(), config.getDateFormat(), isCsv ? config.getDelimiter() : null,
                config.ignoreValues(), htmlExample);
        return ResponseEntity.ok(format);
    }

    @GetMapping(value = "/all", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(description = "Returns the expected format for all bank input files")
    @ApiResponse(responseCode = "200", description = "Format information retrieved successfully")
    public ResponseEntity<List<FileConfigEntity>> getAllBankFileFormats() {
        try {
            return ResponseEntity.ok(fileConfigService.getAllFilesConfigs());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{bankName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update the configuration for a specific bank at runtime")
    @ApiResponse(responseCode = "200", description = "Configuration updated successfully")
    @ApiResponse(responseCode = "400", description = "Unknown bank name")
    public ResponseEntity<Void> updateConfig(@PathVariable String bankName,
                                             @RequestBody UpdateFileConfigRequestDto request) {
        fileConfigService.updateFileConfig(bankName, request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Add a new configuration for a bank")
    @ApiResponse(responseCode = "200", description = "Configuration added successfully")
    public ResponseEntity<Void> addConfig(@RequestBody AddFileConfigRequestDto request) {
        fileConfigService.addFileConfig(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{bankName}")
    @Operation(description = "Delete the bank configuration")
    @ApiResponse(responseCode = "200", description = "Configuration removed successfully")
    public ResponseEntity<Void> deleteBankFileFormat(@PathVariable String bankName) {
        fileConfigService.deleteFileConfig(bankName);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}