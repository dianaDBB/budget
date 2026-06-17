package com.budget.adapters.rest.dto;

public record BankFileFormatDto(
        String bankName,
        String fileFormat,
        int firstDataLine,
        int dateColumnPosition,
        int amountColumnPosition,
        int descriptionColumnPosition,
        Integer creditDebitColumnPosition,
        String dateFormat,
        String delimiter,
        String htmlExample) {
}

