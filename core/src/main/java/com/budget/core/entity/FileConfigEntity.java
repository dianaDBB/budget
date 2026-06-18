package com.budget.core.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
@Table(name = "file_config")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "first_line")
    private int firstLine;

    @Column(name = "date_column_pos")
    private int dateColumnPos;

    @Column(name = "amount_column_pos")
    private int amountColumnPos;

    @Column(name = "desc_column_pos")
    private int descColumnPos;

    @Column(name = "credit_debit_column_pos")
    private Integer creditDebitColumnPos;

    @Column(name = "date-format")
    private String dateFormat;

    @Column(name = "delimiter")
    private String delimiter;

    @Column(name = "file_format")
    private String fileFormat;

    @Column(name = "ignore_values")
    private String ignoreValues;

    public Set<String> ignoreValues() {
        return (ignoreValues != null && !ignoreValues.isBlank())
                ? Arrays.stream(ignoreValues.split(",")).map(String::trim).collect(Collectors.toSet())
                : Set.of();
    }

    public Date getFormatedDate(String value) throws ParseException {
        return new SimpleDateFormat(dateFormat).parse(value);
    }

    public double getAmount(String value, String creditOrDebit) {
        double amount = Double.parseDouble(value);
        if (creditDebitColumnPos >= 0 && !Objects.equals(creditOrDebit, "N/A")) {
            return creditOrDebit.equals("C") ? Math.abs(amount) : -Math.abs(amount);
        }
        return amount;
    }

    public String getType(double value, String creditOrDebit, String originalDescription) {
        String desc = originalDescription.toUpperCase();
        if (desc.contains("S/ LEVANTAMENTOS")) return "Expense";
        if (desc.contains("TRF P/ DIANA") || desc.contains("LEV ATM") ||
                desc.contains("TRF IMEDIATA DIANA") || desc.contains("TRF A CRÉDITO SEPA+ DIANA") ||
                desc.contains("LEVANT") || desc.contains("EUR DEPOSIT") ||
                desc.contains("LEV.ATM") || desc.contains("LEVANTAMENTO") ||
                desc.contains("TRF.IPS P/ PAULO FILIPE") || desc.contains("TRF P/ PAULO ROCHA") ||
                desc.contains("TRF P/ DIANA BARBOSA")) return "TrasnferInHouse";
        if (!Objects.equals(creditOrDebit, "N/A"))
            return creditOrDebit.equals("C") ? "Income" : "Expense";
        return (value > 0) ? "Income" : "Expense";
    }

    public String createXlsxExample() {
        int maxCol = Stream.of(dateColumnPos, amountColumnPos, descColumnPos, creditDebitColumnPos)
                .max(Integer::compareTo).orElse(0);

        StringBuilder html = new StringBuilder("""
                <style>
                    table { border: 1px; border-right: 1px solid black; border-bottom: 1px solid black; border-collapse: collapse; }
                    td,th { border-left:1px solid black; border-top:1px solid black; padding: 4px; }
                    .excelColum { text-align: center; vertical-align: middle; background: Gainsboro; color: Grey; }
                    .excelRow { text-align: left; vertical-align: middle; background: Gainsboro; color: Grey; }
                </style>
                <table><tr><td> </td>""");

        for (int i = 0; i <= maxCol; i++)
            html.append("<td class=\"excelColum\">").append((char) ('A' + i)).append("</td>");
        html.append("</tr>");

        for (int i = 0; i < firstLine - 1; i++)
            html.append("<tr><td class=\"excelRow\">").append(i + 1).append("</td>")
                    .append("<td colspan=\"").append(maxCol + 1).append("\">Row ").append(i + 1).append("</td></tr>");

        Map<Integer, String> headers = Map.of(dateColumnPos, "Date", amountColumnPos, "Amount",
                descColumnPos, "Description", creditDebitColumnPos, "Credit/Debit");
        html.append("<tr><td class=\"excelRow\">").append(firstLine).append("</td>");
        for (int i = 0; i <= maxCol; i++) html.append("<th>").append(headers.getOrDefault(i, "NA")).append("</th>");
        html.append("</tr>");

        Map<Integer, String> data = Map.of(dateColumnPos,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat)),
                amountColumnPos, "-11.40", descColumnPos, "PAG BXVAL- 5004 VIAVERDE", creditDebitColumnPos, "C");
        html.append("<tr><td class=\"excelRow\">").append(firstLine + 1).append("</td>");
        for (int i = 0; i <= maxCol; i++) html.append("<td>").append(data.getOrDefault(i, "NA")).append("</td>");
        html.append("</tr></table>");
        return html.toString();
    }

    public String createCsvExample() {
        int maxCol = Stream.of(dateColumnPos, amountColumnPos, descColumnPos, creditDebitColumnPos)
                .max(Integer::compareTo).orElse(0);

        StringBuilder html = new StringBuilder("""
                <style>
                    table { border: 1px; border-right: 1px solid black; border-bottom: 1px solid black; border-collapse: collapse; }
                    td,th { border-left:1px solid black; border-top:1px solid black; padding: 4px; }
                </style>
                <table>""");

        Map<Integer, String> headers = Map.of(dateColumnPos, "Date", amountColumnPos, "Amount",
                descColumnPos, "Description", creditDebitColumnPos, "Credit/Debit");
        html.append("<tr>");
        for (int i = 0; i <= maxCol; i++) html.append("<th>").append(headers.getOrDefault(i, "NA")).append("</th>");
        html.append("</tr>");

        Map<Integer, String> data = Map.of(dateColumnPos,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern(dateFormat)),
                amountColumnPos, "-11.40", descColumnPos, "PAG BXVAL- 5004 VIAVERDE", creditDebitColumnPos, "C");
        html.append("<tr>");
        for (int i = 0; i <= maxCol; i++) html.append("<td>").append(data.getOrDefault(i, "NA")).append("</td>");
        html.append("</tr></table>");
        return html.toString();
    }
}
