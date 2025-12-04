package com.l3ProblemStatement.dto;

import lombok.Data;

@Data
public class ReviewReqDto {
    private int points;
    private String comment;
    private Long userId;
}
