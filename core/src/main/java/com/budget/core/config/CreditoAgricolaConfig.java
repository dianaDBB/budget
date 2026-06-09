package com.budget.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

@Component
@ConfigurationProperties(prefix = "credito-agricola")
public class CreditoAgricolaConfig implements BankConfig {
    private String name;
    private int firstLine;
    private int dateColumnPosition;
    private int amountColumnPosition;
    private int descriptionColumnPosition;
    private String dateFormat;
    private String delimiter;
    private int cdColumnPosition;

    @Override
    public Set<String> ignoreValues() {
        return Set.of("Nome Ordenante", "NIB/IBAN/Conta Ordenante", "Nome do Beneficiário", "Referência");
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
    public int getCdColumnPosition() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setFirstLine(int firstLine) {
        this.firstLine = firstLine;
    }

    public void setDateColumnPosition(int dateColumnPosition) {
        this.dateColumnPosition = dateColumnPosition;
    }

    public void setAmountColumnPosition(int amountColumnPosition) {
        this.amountColumnPosition = amountColumnPosition;
    }

    public void setDescriptionColumnPosition(int descriptionColumnPosition) {
        this.descriptionColumnPosition = descriptionColumnPosition;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setCdColumnPosition(int cdColumnPosition) {
        this.cdColumnPosition = cdColumnPosition;
    }
}
