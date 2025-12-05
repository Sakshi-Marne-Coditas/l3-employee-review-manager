package com.l3ProblemStatement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewReqDto {

    @NotNull(message = "Points are required")
    @Positive(message = "Points must be greater than 0")
    private int points;

    @NotBlank(message = "Comment cannot be blank")
    private String comment;

    @NotNull(message = "EmployeeId is required")
    @Positive(message = "EmployeeId must be a valid positive number")
    private Long employeeId;
}
