package com.l3ProblemStatement.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class EmployeeDocsUpdateDto {
    @NotNull(message = "employee id can't be blank")
    @Positive(message = "employee id must be positive")
    private Long employeeId;

    private MultipartFile resume;

    @Valid
    private List<CertificateUploadDto> certificates;
}
