package com.budget.adapters.rest;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        try {
            FileConfigEntity config = fileConfigService.getBankFileConfig(bankName);

            boolean isCsv = "CSV".equals(config.getFileFormat());
            String htmlExample = isCsv ? config.createCsvExample() : config.createXlsxExample();
            GetBankFileFormatResponseDto format = new GetBankFileFormatResponseDto(
                    config.getBankName(),
                    isCsv ? "CSV" : "XLSX",
                    config.getFirstLine(),
                    config.getDateColumnPos() + 1,
                    config.getAmountColumnPos() + 1,
                    config.getDescColumnPos() + 1,
                    config.creditDebitColumnPosOrDefault() == -1 ? null : config.creditDebitColumnPosOrDefault() + 1,
                    config.getDateFormat(),
                    isCsv ? config.getDelimiter() : null,
                    htmlExample);
            return ResponseEntity.ok(format);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{bankName}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Update the configuration for a specific bank at runtime")
    @ApiResponse(responseCode = "200", description = "Configuration updated successfully")
    @ApiResponse(responseCode = "400", description = "Unknown bank name")
    public ResponseEntity<Void> updateConfig(
            @PathVariable String bankName,
            @RequestBody UpdateFileConfigRequestDto request) {
        try {
            fileConfigService.updateBankFileConfig(bankName, request);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}