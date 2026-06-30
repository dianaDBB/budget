package com.budget.core.dto;

import lombok.Data;

import java.util.Set;

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
    private Set<String> ignoreValues;
    private String htmlExample;

    public GetBankFileFormatResponseDto(String bankName, String fileFormat, int firstDataLine, int dateColumnPosition
            , int amountColumnPosition, int descriptionColumnPosition, Integer creditDebitColumnPosition,
                                        String dateFormat, String delimiter, Set<String> ignoreValues,
                                        String htmlExample) {
        this.bankName = bankName;
        this.fileFormat = fileFormat;
        this.firstDataLine = firstDataLine;
        this.dateColumnPosition = dateColumnPosition;
        this.amountColumnPosition = amountColumnPosition;
        this.descriptionColumnPosition = descriptionColumnPosition;
        this.creditDebitColumnPosition = creditDebitColumnPosition;
        this.dateFormat = dateFormat;
        this.delimiter = delimiter;
        this.ignoreValues = ignoreValues;
        this.htmlExample = htmlExample;
    }
}

