package com.budget.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class CreditoAgricolaConfig implements BankConfig {
    public String name;
    public int firstLine;
    public int dateColumnPosition;
    public int amountColumnPosition;
    public int descriptionColumnPosition;
    public String dateFormat;
    public String delimiter;
    public int cdColumnPosition;

    public CreditoAgricolaConfig(
            @Value("${creditoAgricola.name}") String name,
            @Value("${creditoAgricola.firstLine}") int firstLine,
            @Value("${creditoAgricola.dateColumnPosition}") int dateColumnPosition,
            @Value("${creditoAgricola.amountColumnPosition}") int amountColumnPosition,
            @Value("${creditoAgricola.descriptionColumnPosition}") int descriptionColumnPosition,
            @Value("${creditoAgricola.dateFormat}") String dateFormat,
            @Value("${creditoAgricola.delimiter}") String delimiter,
            @Value("${creditoAgricola.cdColumnPosition}") int cdColumnPosition) {
        this.name = name;
        this.firstLine = firstLine;
        this.dateColumnPosition = dateColumnPosition;
        this.amountColumnPosition = amountColumnPosition;
        this.descriptionColumnPosition = descriptionColumnPosition;
        this.dateFormat = dateFormat;
        this.delimiter = delimiter;
        this.cdColumnPosition = cdColumnPosition;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getFirstLine() {
        return this.firstLine;
    }

    @Override
    public String getDelimiter() {
        return this.delimiter;
    }

    @Override
    public int getAmountColumnPosition() {
        return this.amountColumnPosition;
    }

    @Override
    public int getDateColumnPosition() {
        return this.dateColumnPosition;
    }

    @Override
    public int getDescriptionColumnPosition()
    {
        return this.descriptionColumnPosition;
    }

    @Override
    public int getCDColumnPosition() {
        return this.cdColumnPosition;
    }

    @Override
    public Date getDate(String value) throws ParseException {
        String dateFormat = this.dateFormat;
        return (new SimpleDateFormat(dateFormat)).parse(value);
    }

    @Override
    public double getAmount(String value, String creditOrDebit) {
        if(creditOrDebit.equals("C")) {
            return Double.parseDouble(value);
        }

        return Double.parseDouble(value) * -1;
    }

    @Override
    public String getType(double value, String creditOrDebit) {
        return (creditOrDebit.equals("C")) ? "Income" : "Expense";
    }

    @Override
    public String getCategory() {
        // TODO: we can add here some logic to get the category
        return "";
    }

    @Override
    public String getSubCategory() {
        // TODO: we can add here some logic to get the sub-category
        return "";
    }
}
