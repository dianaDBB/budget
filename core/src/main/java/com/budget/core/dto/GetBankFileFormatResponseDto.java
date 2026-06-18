package com.budget.core.dto;

public class GetBankFileFormatResponseDto {
    private String bankName;
    private String fileFormat;
    private int firstDataLine;
    private int dateColumnPosition;
    private int amountColumnPosition;
    private int descriptionColumnPosition;
    private Integer creditDebitColumnPosition;
    private String dateFormat;
    private String delimiter;
    private String htmlExample;

    public GetBankFileFormatResponseDto(String bankName, String fileFormat, int firstDataLine, int dateColumnPosition,
                                        int amountColumnPosition, int descriptionColumnPosition,
                                        Integer creditDebitColumnPosition,
                                        String dateFormat, String delimiter, String htmlExample) {
        this.bankName = bankName;
        this.fileFormat = fileFormat;
        this.firstDataLine = firstDataLine;
        this.dateColumnPosition = dateColumnPosition;
        this.amountColumnPosition = amountColumnPosition;
        this.descriptionColumnPosition = descriptionColumnPosition;
        this.creditDebitColumnPosition = creditDebitColumnPosition;
        this.dateFormat = dateFormat;
        this.delimiter = delimiter;
        this.htmlExample = htmlExample;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public int getFirstDataLine() {
        return firstDataLine;
    }

    public void setFirstDataLine(int firstDataLine) {
        this.firstDataLine = firstDataLine;
    }

    public int getDateColumnPosition() {
        return dateColumnPosition;
    }

    public void setDateColumnPosition(int dateColumnPosition) {
        this.dateColumnPosition = dateColumnPosition;
    }

    public int getAmountColumnPosition() {
        return amountColumnPosition;
    }

    public void setAmountColumnPosition(int amountColumnPosition) {
        this.amountColumnPosition = amountColumnPosition;
    }

    public int getDescriptionColumnPosition() {
        return descriptionColumnPosition;
    }

    public void setDescriptionColumnPosition(int descriptionColumnPosition) {
        this.descriptionColumnPosition = descriptionColumnPosition;
    }

    public Integer getCreditDebitColumnPosition() {
        return creditDebitColumnPosition;
    }

    public void setCreditDebitColumnPosition(Integer creditDebitColumnPosition) {
        this.creditDebitColumnPosition = creditDebitColumnPosition;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getHtmlExample() {
        return htmlExample;
    }

    public void setHtmlExample(String htmlExample) {
        this.htmlExample = htmlExample;
    }
}

