package com.budget.core.config;

import java.text.ParseException;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

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
}
