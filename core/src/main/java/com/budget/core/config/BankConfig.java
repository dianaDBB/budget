package com.budget.core.config;

import java.text.ParseException;
import java.util.Date;

public interface BankConfig {
    String getName();
    int getFirstLine();
    String getDelimiter();
    int getAmountColumnPosition();
    int getDateColumnPosition();
    int getDescriptionColumnPosition();
    int getCDColumnPosition();
    Date getDate(String value) throws ParseException;
    double getAmount(String value, String creditOrDebit);
    String getType(double value, String creditOrDebit);
    String getCategory();
    String getSubCategory();
}
