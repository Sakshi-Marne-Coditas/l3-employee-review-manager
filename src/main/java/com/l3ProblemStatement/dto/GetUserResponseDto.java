package com.l3ProblemStatement.dto;

import lombok.Data;

@Data
public class GetUserResponseDto {
    private Long userId;
    private String userName;
    private String email;
    private String role;
    private String designation;
    private String profilePhoto;
}
