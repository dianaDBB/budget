package com.budget.core.config;

import java.text.ParseException;
import java.time.LocalDate;
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

    default String[] getCategory(String value) {
        if (value.toUpperCase().contains("S/ LEVANTAMENTOS")) {
            return new String[]{"Bank", "Fees"};
        } else if (value.toUpperCase().contains("VODAFONE")) {
            return new String[]{"Utilities", "Internet"};
        } else if (value.toUpperCase().contains("BALTAREJO")) {
            return new String[]{"Daily_Livings", "Gym"};
        } else if (value.toUpperCase().contains("IDEIAS DECIMAIS")) {
            return new String[]{"House_Construction", "Contractor"};
        } else if (value.toUpperCase().contains("COMISSÃO TRANSFERÊNCIA")) {
            return new String[]{"Bank", "Fees"};
        } else if (value.toUpperCase().contains("AGUAS VALONGO")) {
            return new String[]{"Utilities", "Water"};
        } else if (value.toUpperCase().contains("IMP.  SELO")) {
            return new String[]{"Bank", "Fees"};
        } else if (value.toUpperCase().contains("UBER    EATS")) {
            return new String[]{"Daily_Livings", "Dining Out"};
        } else if (value.toUpperCase().contains("UBER  EATS")) {
            return new String[]{"Daily_Livings", "Dining Out"};
        } else if (value.toUpperCase().contains("COMPRA AUTO REP MOU")) {
            return new String[]{"Utilities", "Gas"};
        } else if (value.toUpperCase().contains("COMPRA BURGER KING")) {
            return new String[]{"Daily_Livings", "Dining Out"};
        } else if (value.toUpperCase().contains("COMPRA CHURRASQ")) {
            return new String[]{"Daily_Livings", "Dining Out"};
        } else if (value.toUpperCase().contains("COMPRA CONTINENTE")) {
            return new String[]{"Home", "Groceries"};
        } else if (value.toUpperCase().contains("COMPRA DISTRICOURA")) {
            return new String[]{"Home", "Groceries"};
        } else if (value.toUpperCase().contains("COMPRA INTERMARCHE")) {
            return new String[]{"Home", "Groceries"};
        } else if (value.toUpperCase().contains("COMPRA INTERVALONGO")) {
            return new String[]{"Home", "Groceries"};
        } else if (value.toUpperCase().contains("COMPRA KFC")) {
            return new String[]{"Daily_Livings", "Dining Out"};
        } else if (value.toUpperCase().contains("COMPRA MCD")) {
            return new String[]{"Daily_Livings", "Dining Out"};
        } else if (value.toUpperCase().contains("COMPRA PINGO DOCE")) {
            return new String[]{"Home", "Groceries"};
        } else if (value.toUpperCase().contains("COMPRA STAPLES")) {
            return new String[]{"Job´s", "Supplies"};
        } else if (value.toUpperCase().contains("ALLEGIS GROUP")) {
            return new String[]{"", ""};
        } else if (value.toUpperCase().contains("CONTINENTE")) {
            return new String[]{"Home", "Groceries"};
        } else if (value.toUpperCase().contains("POSTO ABASTECIMENTO")) {
            return new String[]{"Car", "Fuel"};
        } else if (value.toUpperCase().contains("INTERVALONGO")) {
            return new String[]{"Home", "Groceries"};
        } else if (value.toUpperCase().contains("FARMACIA")) {
            return new String[]{"Daily_Livings", "Health"};
        } else if (value.toUpperCase().contains("NETFLIX")) {
            return new String[]{"Daily_Livings", "Streaming"};
        } else if (value.toUpperCase().contains("LIDL")) {
            return new String[]{"Home", "Groceries"};
        } else if (value.toUpperCase().contains("GALP")) {
            return new String[]{"Car", "Fuel"};
        } else if (value.toUpperCase().contains("MERCADONA")) {
            return new String[]{"Home", "Groceries"};
        } else if (value.toUpperCase().contains("CRV*WELL")) {
            return new String[]{"Daily_Livings", "Health"};
        } else if (value.toUpperCase().contains("GLOVO")) {
            return new String[]{"Daily_Livings", "Dining Out"};
        } else if (value.toUpperCase().contains("CRV*MCD")) {
            return new String[]{"Daily_Livings", "Dining Out"};
        } else if (value.toUpperCase().contains("PINGO DOCE")) {
            return new String[]{"Home", "Groceries"};
        } else if (value.toUpperCase().contains("RESTAURANTE")) {
            return new String[]{"Daily_Livings", "Dining Out"};
        } else if (value.toUpperCase().contains("SUPERMERCADO")) {
            return new String[]{"Home", "Groceries"};
        } else if (value.toUpperCase().contains("SPOTIFY")) {
            return new String[]{"Daily_Livings", "Streaming"};
        } else if (value.toUpperCase().contains("VIA VERDE")) {
            return new String[]{"Car", "Tolls"};
        } else if (value.toUpperCase().contains("COMISSÃO S/")) {
            return new String[]{"Bank", "Fees"};
        } else if (value.toUpperCase().contains("I.S. COM.")) {
            return new String[]{"Bank", "Fees"};
        } else if (value.toUpperCase().contains("I.SELO")) {
            return new String[]{"Bank", "Fees"};
        } else if (value.toUpperCase().contains("MAIS CONSIGO")) {
            return new String[]{"Bank", "Fees"};
        } else if (value.toUpperCase().contains("NOS COM")) {
            return new String[]{"Utilities", "Phone"};
        } else if (value.toUpperCase().contains("TRF.     0000351 00938121242")) {
            return new String[]{"Home", "Loan"};
        } else if (value.toUpperCase().contains("POSTO ABASTECIMENT")) {
            return new String[]{"Car", "Fuel"};
        }

        return new String[]{"", ""};
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
        for (int i = 0; i < maxCol; i++) {
            char letter = (char) ('A' + i);

            htmlExample.append("<td class=\"excelColum\">").append(letter).append("</td>");
        }
        htmlExample.append("</tr>");

        // add rows until first data row
        for (int i = 0; i < firstDataLine; i++) {
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
        for (int i = 0; i < maxCol; i++) {
            htmlExample.append("<th>")
                    .append(headers.getOrDefault(i, "NA"))
                    .append("</th>");
        }
        htmlExample.append("</tr>");

        // add bank data
        htmlExample.append("<tr>");
        htmlExample.append("<td class=\"excelRow\">").append(firstDataLine + 1).append("</td>");
        Map<Integer, String> data = Map.of(
                dateCol, LocalDate.now().format(DateTimeFormatter.ofPattern(dateFormat)),
                amountCol, "-11.40",
                descCol, "PAG BXVAL- 5004 VIAVERDE"
        );
        for (int i = 0; i < maxCol; i++) {
            htmlExample.append("<td>")
                    .append(data.getOrDefault(i, "NA"))
                    .append("</td>");
        }
        htmlExample.append("</tr>");

        htmlExample.append("</table>");

        return htmlExample.toString();
    }
}
