package com.budget.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@ConfigurationProperties(prefix = "crypto-com")
public class CryptoComConfig implements BankConfig {
    private String name;
    private int firstLine;
    private int dateColumnPosition;
    private int amountColumnPosition;
    private int descriptionColumnPosition;
    private String dateFormat;
    private String delimiter;
    private int cdColumnPosition;

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
    public int getDescriptionColumnPosition() {
        return this.descriptionColumnPosition;
    }

    @Override
    public int getCdColumnPosition() {
        return this.cdColumnPosition;
    }

    @Override
    public String getDateFormat() {
        return this.dateFormat;
    }

    @Override
    public void update(BankConfigRequest request) {
        if (request.getFirstLine() != null) this.firstLine = request.getFirstLine();
        if (request.getDelimiter() != null) this.delimiter = request.getDelimiter();
        if (request.getDateFormat() != null) this.dateFormat = request.getDateFormat();
        if (request.getAmountColumnPosition() != null) this.amountColumnPosition = request.getAmountColumnPosition();
        if (request.getDateColumnPosition() != null) this.dateColumnPosition = request.getDateColumnPosition();
        if (request.getDescriptionColumnPosition() != null)
            this.descriptionColumnPosition = request.getDescriptionColumnPosition();
        if (request.getCdColumnPosition() != null) this.cdColumnPosition = request.getCdColumnPosition();
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
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setFirstLine(int firstLine) {
        this.firstLine = firstLine;
    }

    @Override
    public void setDateColumnPosition(int dateColumnPosition) {
        this.dateColumnPosition = dateColumnPosition;
    }

    @Override
    public void setAmountColumnPosition(int amountColumnPosition) {
        this.amountColumnPosition = amountColumnPosition;
    }

    @Override
    public void setDescriptionColumnPosition(int descriptionColumnPosition) {
        this.descriptionColumnPosition = descriptionColumnPosition;
    }

    @Override
    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public void setCdColumnPosition(int cdColumnPosition) {
        this.cdColumnPosition = cdColumnPosition;
    }
}
