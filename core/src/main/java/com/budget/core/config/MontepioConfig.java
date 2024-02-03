package com.budget.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
public class MontepioConfig implements BankConfig {
    public String name;
    public int firstLine;
    public int dateColumnPosition;
    public int amountColumnPosition;
    public int descriptionColumnPosition;
    public String dateFormat;
    public String delimiter;
    public int cdColumnPosition;

    public MontepioConfig(
            @Value("${montepio.name}") String name,
            @Value("${montepio.firstLine}") int firstLine,
            @Value("${montepio.dateColumnPosition}") int dateColumnPosition,
            @Value("${montepio.amountColumnPosition}") int amountColumnPosition,
            @Value("${montepio.descriptionColumnPosition}") int descriptionColumnPosition,
            @Value("${montepio.dateFormat}") String dateFormat,
            @Value("${montepio.delimiter}") String delimiter,
            @Value("${montepio.cdColumnPosition}") int cdColumnPosition) {
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
        return Double.parseDouble(value.replace(".", "").replace(",", "."));
    }
}
