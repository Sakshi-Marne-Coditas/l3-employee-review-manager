package com.l3ProblemStatement.dto;

import com.l3ProblemStatement.constants.RoleName;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserReqDto {
    private String name;
    private String password;
    private String email;
    private String designation;
    private double salary;
    private Long departmentId;
    private RoleName role;
    private MultipartFile profilePhoto;
}
