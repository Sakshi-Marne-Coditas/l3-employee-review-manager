package com.l3ProblemStatement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDepartmentReqDto {

    @NotBlank(message = "Department name is required")
    private String name;
}
