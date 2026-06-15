package com.budget.core.config;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public interface BankConfig {
    default Set<String> ignoreValues() {
        return Set.of();
    }

    String getName();

    int getFirstLine();

    String getDelimiter();

    String getDateFormat();

    int getAmountColumnPosition();

    int getDateColumnPosition();

    int getDescriptionColumnPosition();

    int getCdColumnPosition();

    Date getDate(String value) throws ParseException;

    double getAmount(String value, String creditOrDebit);

    default String getType(double value, String creditOrDebit, String originalDescription) {
        if (originalDescription.toUpperCase().contains("S/ LEVANTAMENTOS")) {
            return "Expense";
        }
        if (originalDescription.toUpperCase().contains("TRF P/ DIANA") ||
                originalDescription.toUpperCase().contains("LEV ATM") ||
                originalDescription.toUpperCase().contains("TRF IMEDIATA DIANA") ||
                originalDescription.toUpperCase().contains("TRF A CRÉDITO SEPA+ DIANA") ||
                originalDescription.toUpperCase().contains("LEVANT") ||
                originalDescription.toUpperCase().contains("EUR DEPOSIT") ||
                originalDescription.toUpperCase().contains("LEV.ATM") ||
                originalDescription.toUpperCase().contains("LEVANTAMENTO") ||
                originalDescription.toUpperCase().contains("TRF.IPS P/ PAULO FILIPE") ||
                originalDescription.toUpperCase().contains("TRF P/ PAULO ROCHA") ||
                originalDescription.toUpperCase().contains("TRF P/ DIANA BARBOSA")
        ) {
            return "TrasnferInHouse";
        }

        if (!Objects.equals(creditOrDebit, "N/A")) {
            return (creditOrDebit.equals("C")) ? "Income" : "Expense";
        }

        return (value > 0) ? "Income" : "Expense";
    }

    void setName(String name);

    void setFirstLine(int firstLine);

    void setDateColumnPosition(int dateColumnPosition);

    void setAmountColumnPosition(int amountColumnPosition);

    void setDescriptionColumnPosition(int descriptionColumnPosition);

    void setDateFormat(String dateFormat);

    void setDelimiter(String delimiter);

    void setCdColumnPosition(int cdColumnPosition);

    void update(BankConfigRequest request);

    default String createXlsxExample(BankConfig config) {
        StringBuilder htmlExample = new StringBuilder("""
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
                """);

        var dateCol = config.getDateColumnPosition();
        var amountCol = config.getAmountColumnPosition();
        var descCol = config.getDescriptionColumnPosition();
        var creditDebitCol = config.getCdColumnPosition();
        var firstDataLine = config.getFirstLine();
        var dateFormat = config.getDateFormat();

        int maxCol = Stream.of(dateCol, amountCol, descCol, creditDebitCol).max(Integer::compareTo).orElse(0);

        // add excel column header
        for (int i = 0; i <= maxCol; i++) {
            char letter = (char) ('A' + i);

            htmlExample.append("<td class=\"excelColum\">").append(letter).append("</td>");
        }
        htmlExample.append("</tr>");

        // add rows until first data row
        for (int i = 0; i < firstDataLine - 1; i++) {
            htmlExample.append("<tr>")
                    .append("<td class=\"excelRow\">").append(i + 1).append("</td>")
                    .append("<td colspan=\"").append(maxCol + 1).append("\">").append("Row ").append(i + 1).append("</td>")
                    .append("</tr>");
        }

        // add bank header
        htmlExample.append("<tr>");
        htmlExample.append("<td class=\"excelRow\">").append(firstDataLine).append("</td>");
        Map<Integer, String> headers = Map.of(
                dateCol, "Date",
                amountCol, "Amount",
                descCol, "Description",
                creditDebitCol, "Credit/Debit"
        );
        for (int i = 0; i <= maxCol; i++) {
            htmlExample.append("<th>")
                    .append(headers.getOrDefault(i, "NA"))
                    .append("</th>");
        }
        htmlExample.append("</tr>");

        // add bank data
        htmlExample.append("<tr>");
        htmlExample.append("<td class=\"excelRow\">").append(firstDataLine + 1).append("</td>");
        Map<Integer, String> data = Map.of(
                dateCol, LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat)),
                amountCol, "-11.40",
                descCol, "PAG BXVAL- 5004 VIAVERDE",
                creditDebitCol, "C"
        );
        for (int i = 0; i <= maxCol; i++) {
            htmlExample.append("<td>")
                    .append(data.getOrDefault(i, "NA"))
                    .append("</td>");
        }
        htmlExample.append("</tr>");

        htmlExample.append("</table>");

        return htmlExample.toString();
    }

    default String createCsvExample(BankConfig config) {
        StringBuilder htmlExample = new StringBuilder("""
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
                """);

        var dateCol = config.getDateColumnPosition();
        var amountCol = config.getAmountColumnPosition();
        var descCol = config.getDescriptionColumnPosition();
        var creditDebitCol = config.getCdColumnPosition();
        var dateFormat = config.getDateFormat();

        int maxCol = Stream.of(dateCol, amountCol, descCol, creditDebitCol).max(Integer::compareTo).orElse(0);

        // add bank header
        htmlExample.append("<tr>");
        Map<Integer, String> headers = Map.of(
                dateCol, "Date",
                amountCol, "Amount",
                descCol, "Description",
                creditDebitCol, "Credit/Debit"
        );
        for (int i = 0; i <= maxCol; i++) {
            htmlExample.append("<th>")
                    .append(headers.getOrDefault(i, "NA"))
                    .append("</th>");
        }
        htmlExample.append("</tr>");

        // add bank data
        htmlExample.append("<tr>");
        Map<Integer, String> data = Map.of(
                dateCol, LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat)),
                amountCol, "-11.40",
                descCol, "PAG BXVAL- 5004 VIAVERDE",
                creditDebitCol, "C"
        );
        for (int i = 0; i <= maxCol; i++) {
            htmlExample.append("<td>")
                    .append(data.getOrDefault(i, "NA"))
                    .append("</td>");
        }
        htmlExample.append("</tr>");

        htmlExample.append("</table>");

        return htmlExample.toString();
    }
}
