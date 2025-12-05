package com.l3ProblemStatement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateReviewReqDto {

    @Positive(message = "Points must be greater than 0")
    private int points;

    private String comment;
}
