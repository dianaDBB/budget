package com.budget.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class CryptoComConfig implements BankConfig {
    public String name;
    public int firstLine;
    public int dateColumnPosition;
    public int amountColumnPosition;
    public int descriptionColumnPosition;
    public String dateFormat;
    public String delimiter;
    public int cdColumnPosition;

    public CryptoComConfig(
            @Value("${cryptoCom.name}") String name,
            @Value("${cryptoCom.firstLine}") int firstLine,
            @Value("${cryptoCom.dateColumnPosition}") int dateColumnPosition,
            @Value("${cryptoCom.amountColumnPosition}") int amountColumnPosition,
            @Value("${cryptoCom.descriptionColumnPosition}") int descriptionColumnPosition,
            @Value("${cryptoCom.dateFormat}") String dateFormat,
            @Value("${cryptoCom.delimiter}") String delimiter,
            @Value("${cryptoCom.cdColumnPosition}") int cdColumnPosition) {
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
        return Double.parseDouble(value);
    }

    @Override
    public String getType(double value, String creditOrDebit) {
        return (value > 0) ? "Income" : "Expense";
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
