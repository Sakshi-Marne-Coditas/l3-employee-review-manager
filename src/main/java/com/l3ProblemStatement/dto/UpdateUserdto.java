package com.l3ProblemStatement.dto;

import com.l3ProblemStatement.constants.RoleName;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserdto {
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "Username must start with a letter and contain only letters and numbers"
    )
    private String name;

    @Size(min = 6, max = 100, message = "Confirm password must match password length requirements")
    @Column(nullable = false)
    private String password;


    @Email(message = "Invalid email format")
    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Email must be valid and contain @ and a proper domain like .com"
    )
    private String email;

    private String designation;


    @Positive(message = "salary must be positive")
    private Double salary;


    @Positive(message = "department id must be positive")
    private Long departmentId;

    private RoleName role;

    private MultipartFile profilePhoto;
}
