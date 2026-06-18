package com.budget.core.dto;

import lombok.Data;

@Data
public class UpdateFileConfigRequestDto {
    private Integer firstLine;
    private String delimiter;
    private String dateFormat;
    private Integer amountColumnPosition;
    private Integer dateColumnPosition;
    private Integer descriptionColumnPosition;
    private Integer cdColumnPosition;
}
