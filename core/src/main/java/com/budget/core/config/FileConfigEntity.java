package com.budget.core.config;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "file_config")
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

    @Column(name = "dateformat")
    private String dateformat;

    @Column(name = "delimiter")
    private String delimiter;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(int firstLine) {
        this.firstLine = firstLine;
    }

    public int getDateColumnPos() {
        return dateColumnPos;
    }

    public void setDateColumnPos(int dateColumnPos) {
        this.dateColumnPos = dateColumnPos;
    }

    public int getAmountColumnPos() {
        return amountColumnPos;
    }

    public void setAmountColumnPos(int amountColumnPos) {
        this.amountColumnPos = amountColumnPos;
    }

    public int getDescColumnPos() {
        return descColumnPos;
    }

    public void setDescColumnPos(int descColumnPos) {
        this.descColumnPos = descColumnPos;
    }

    public Integer getCreditDebitColumnPos() {
        return creditDebitColumnPos;
    }

    public void setCreditDebitColumnPos(Integer creditDebitColumnPos) {
        this.creditDebitColumnPos = creditDebitColumnPos;
    }

    public String getDateformat() {
        return dateformat;
    }

    public void setDateformat(String dateformat) {
        this.dateformat = dateformat;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
