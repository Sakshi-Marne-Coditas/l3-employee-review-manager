package com.l3ProblemStatement.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class LoginReqDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(
            regexp = "^[A-Za-z][A-Za-z0-9._-]*@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Email must start with a letter and be valid like example@gmail.com"
    )
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "password cannot be blank")
    @Size(min = 6, max = 100, message = "Confirm password must match password length requirements")
    @Column(nullable = false)
    private String password;
}
