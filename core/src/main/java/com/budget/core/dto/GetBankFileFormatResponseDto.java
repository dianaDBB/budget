package com.budget.core.dto;

import lombok.Data;

@Data
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
}

