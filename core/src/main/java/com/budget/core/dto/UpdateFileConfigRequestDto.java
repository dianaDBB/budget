package com.budget.core.dto;

public class UpdateFileConfigRequestDto {
    private Integer firstLine;
    private String delimiter;
    private String dateFormat;
    private Integer amountColumnPosition;
    private Integer dateColumnPosition;
    private Integer descriptionColumnPosition;
    private Integer cdColumnPosition;

    public Integer getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(Integer firstLine) {
        this.firstLine = firstLine;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public Integer getAmountColumnPosition() {
        return amountColumnPosition;
    }

    public void setAmountColumnPosition(Integer amountColumnPosition) {
        this.amountColumnPosition = amountColumnPosition;
    }

    public Integer getDateColumnPosition() {
        return dateColumnPosition;
    }

    public void setDateColumnPosition(Integer dateColumnPosition) {
        this.dateColumnPosition = dateColumnPosition;
    }

    public Integer getDescriptionColumnPosition() {
        return descriptionColumnPosition;
    }

    public void setDescriptionColumnPosition(Integer descriptionColumnPosition) {
        this.descriptionColumnPosition = descriptionColumnPosition;
    }

    public Integer getCdColumnPosition() {
        return cdColumnPosition;
    }

    public void setCdColumnPosition(Integer cdColumnPosition) {
        this.cdColumnPosition = cdColumnPosition;
    }
}
