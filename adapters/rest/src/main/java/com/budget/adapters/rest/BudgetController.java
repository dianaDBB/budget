package com.budget.adapters.rest;

import com.budget.core.BudgetService;
import com.budget.core.config.BankConfig;
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
import org.springframework.web.bind.annotation.PostMapping;
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
                    "dd-MMMM-yyyy",
                    null,
                    """
                            <style>
                                table
                                {
                                    border: 1px;
                                    border-right: 1px solid black;
                                    border-bottom: 1px solid black;
                                    border-collapse: collapse;
                                }
                                td,th
                                {
                                    border-left:1px solid black;
                                    border-top:1px solid black;
                                    padding: 4px;
                                }
                                .excelColum
                                {
                                    text-align: center;
                                    vertical-align: middle;
                                    background: Gainsboro;
                                    color: Grey;
                                }
                                .excelRow
                                {
                                    text-align: left;
                                    vertical-align: middle;
                                    background: Gainsboro;
                                    color: Grey;
                                }
                            </style>
                            <table>
                                <tr>
                                    <td> </td>
                                    <td class="excelColum">A</td>
                                    <td class="excelColum">B</td>
                                    <td class="excelColum">C</td>
                                    <td class="excelColum">D</td>
                                    <td class="excelColum">E</td>
                                </tr>
                                <tr><td class="excelRow">1</td><td colspan = "5">HISTÓRICO DE CONTA NÚMERO 123456</td></tr>
                                <tr><td class="excelRow">2</td><td colspan = "5">Moeda: EUR</td></tr>
                                <tr><td class="excelRow">3</td><td colspan = "5"> </td></tr>
                                <tr><td class="excelRow">4</td><td colspan = "5">Tipo: Todos</td></tr>
                                <tr><td class="excelRow">5</td><td colspan = "5">Data de: 01/05/2026</td></tr>
                                <tr><td class="excelRow">6</td><td colspan = "5">Data até: 31/05/2026</td></tr>
                                <tr><td class="excelRow">7</td><td colspan = "5"> </td></tr>
                                <tr>
                                    <td class="excelRow">8</td>
                                    <th>Data Lanc.</th>
                                    <th>Data Valor</th>
                                    <th>Descrição</th>
                                    <th>Valor</th>
                                    <th>Saldo</th>
                                </tr>
                                <tr>
                                    <td class="excelRow">9</td>
                                    <td>01-05-2026</td>
                                    <td>02-05-2026</td>
                                    <td>PAG BXVAL- 5004 VIAVERDE</td>
                                    <td>-11.40</td>
                                    <td>156.34</td>
                                </tr>
                            </table>
                            """);
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
                    "dd/MM/yyyy",
                    null,
                    """
                            <style>
                                table
                                {
                                    border: 1px;
                                    border-right: 1px solid black;
                                    border-bottom: 1px solid black;
                                    border-collapse: collapse;
                                }
                                td,th
                                {
                                    border-left:1px solid black;
                                    border-top:1px solid black;
                                    padding: 4px;
                                }
                                .excelColum
                                {
                                    text-align: center;
                                    vertical-align: middle;
                                    background: Gainsboro;
                                    color: Grey;
                                }
                                .excelRow
                                {
                                    text-align: left;
                                    vertical-align: middle;
                                    background: Gainsboro;
                                    color: Grey;
                                }
                            </style>
                            <table>
                                <tr>
                                    <td> </td>
                                    <td class="excelColum">A</td>
                                    <td class="excelColum">B</td>
                                    <td class="excelColum">C</td>
                                    <td class="excelColum">D</td>
                                    <td class="excelColum">E</td>
                                    <td class="excelColum">F</td>
                                    <td class="excelColum">G</td>
                                </tr>
                                <tr><td class="excelRow">1</td><td colspan = "7">Operação: Consulta de Movimentos de Contas D.O.</td></tr>
                                <tr><td class="excelRow">2</td><td colspan = "7">Conta: 123456</td></tr>
                                <tr><td class="excelRow">3</td><td colspan = "7">De: 30/04/2026</td></tr>
                                <tr><td class="excelRow">4</td><td colspan = "7">A: 31/05/2026</td></tr>
                                <tr><td class="excelRow">5</td><td colspan = "7"> </td></tr>
                                <tr>
                                    <td class="excelRow">6</td>
                                    <th>Data de Movimento</th>
                                    <th>Data Valor</th>
                                    <th>Descrição</th>
                                    <th>Descritivo</th>
                                    <th>Montante</th>
                                    <th>D/C</th>
                                    <th>Saldo Contabilístico</th>
                                </tr>
                                <tr>
                                    <td class="excelRow">7</td>
                                    <td>01/05/2026</td>
                                    <td>02/05/2026</td>
                                    <td>PAG BXVAL- 5004 VIAVERDE</td>
                                    <td>PAG BXVAL- 5004 VIAVERDE</td>
                                    <td>11.40</td>
                                    <td>D</td>
                                    <td>156.34</td>
                                </tr>
                            </table>
                            """);
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
                    "yyyy-MM-dd hh:mm:ss",
                    config.getDelimiter(),
                    """
                            <style>
                                table
                                {
                                    border: 1px;
                                    border-right: 1px solid black;
                                    border-bottom: 1px solid black;
                                    border-collapse: collapse;
                                }
                                td,th
                                {
                                    border-left:1px solid black;
                                    border-top:1px solid black;
                                    padding: 4px;
                                }
                            </style>
                            <table>
                                <tr>
                                    <th>Timestamp (UTC)</th>
                                    <th>Transaction Description</th>
                                    <th>Currency</th>
                                    <th>Amount</th>
                                    <th>To Currency</th>
                                    <th>To Amount</th>
                                    <th>Native Currency</th>
                                    <th>Native Amount</th>
                                    <th>Native Amount (in USD)</th>
                                    <th>Transaction Kind</th>
                                    <th>Transaction Hash</th>
                                </tr>
                                <tr>
                                    <td>2026-05-02 12:36:36</td>
                                    <td>PAG BXVAL- 5004 VIAVERDE</td>
                                    <td>EUR</td>
                                    <td>-11.40</td>
                                    <td> </td>
                                    <td> </td>
                                    <td>EUR</td>
                                    <td>-11.40</td>
                                    <td>-12.42</td>
                                    <td> </td>
                                    <td> </td>
                                </tr>
                            </table>
                            """);
            return ResponseEntity.ok(format);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}