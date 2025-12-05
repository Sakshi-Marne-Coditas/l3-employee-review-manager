package com.l3ProblemStatement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CertificateUploadDto {
    @NotBlank(message = "Certificate name cannot be blank")
    private String certificateName;
    @NotNull(message = "Certificate file is required")
    private MultipartFile certificateFile;
}
