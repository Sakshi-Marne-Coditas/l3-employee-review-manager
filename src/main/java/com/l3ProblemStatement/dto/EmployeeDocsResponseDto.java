package com.l3ProblemStatement.dto;

import lombok.Data;
import java.util.List;

@Data
public class EmployeeDocsResponseDto {

    private Long docsId;

    private String resumeUrl;

    private List<CertificateResponseDto> certificates;
}
