package com.l3ProblemStatement.dto;

import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class EmployeeDocsUploadDto {

    private Long employeeId;

    private MultipartFile resume;

    @Valid
    private List<CertificateUploadDto> certificates;
}