package com.budget.core.dto;

import lombok.Data;

@Data
public class AddFileConfigRequestDto {
    private String bankName;
    private String fileFormat;
    private Integer firstLine;
    private String delimiter;
    private String dateFormat;
    private Integer amountColumnPosition;
    private Integer dateColumnPosition;
    private Integer descColumnPosition;
    private Integer cdColumnPosition;
}
