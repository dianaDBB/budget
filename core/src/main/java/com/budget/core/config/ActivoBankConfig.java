package com.budget.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class ActivoBankConfig implements BankConfig {
    public String name;
    public int firstLine;
    public int dateColumnPosition;
    public int amountColumnPosition;
    public int descriptionColumnPosition;
    public String dateFormat;
    public String delimiter;
    public int cdColumnPosition;

    public ActivoBankConfig(
            @Value("${activoBank.name}") String name,
            @Value("${activoBank.firstLine}") int firstLine,
            @Value("${activoBank.dateColumnPosition}") int dateColumnPosition,
            @Value("${activoBank.amountColumnPosition}") int amountColumnPosition,
            @Value("${activoBank.descriptionColumnPosition}") int descriptionColumnPosition,
            @Value("${activoBank.dateFormat}") String dateFormat,
            @Value("${activoBank.delimiter}") String delimiter,
            @Value("${activoBank.cdColumnPosition}") int cdColumnPosition) {
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
}
