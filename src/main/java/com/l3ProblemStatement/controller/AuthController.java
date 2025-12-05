package com.l3ProblemStatement.controller;

import com.l3ProblemStatement.dto.LoginReqDto;
import com.l3ProblemStatement.dto.LoginResdto;
import com.l3ProblemStatement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    @PostMapping("/login")
    public LoginResdto login(@Validated @RequestBody LoginReqDto request){

        return userService.login(request);

    }

}
