package com.l3ProblemStatement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CertificateResponseDto {

    private Long certificateId;

    private String certificateName;

    private String certificateUrl;
}
