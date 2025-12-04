package com.l3ProblemStatement.service;

import com.l3ProblemStatement.constants.RoleName;
import com.l3ProblemStatement.constants.UserStatus;
import com.l3ProblemStatement.dto.*;
import com.l3ProblemStatement.entity.Department;
import com.l3ProblemStatement.entity.PerformanceReview;
import com.l3ProblemStatement.entity.Role;
import com.l3ProblemStatement.entity.User;
import com.l3ProblemStatement.exceptionHandling.ElementNotFoundExceprion;
import com.l3ProblemStatement.exceptionHandling.EmailAlreadyExistException;
import com.l3ProblemStatement.exceptionHandling.InActiveUserException;
import com.l3ProblemStatement.repository.DepartmentRepository;
import com.l3ProblemStatement.repository.PerformanceReviewRepository;
import com.l3ProblemStatement.repository.RoleRepository;
import com.l3ProblemStatement.repository.UserRepository;
import com.l3ProblemStatement.security.CustomUserDetails;
import com.l3ProblemStatement.security.CustomUserDetailsService;
import com.l3ProblemStatement.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DepartmentRepository departmentRepository;
    private final CloudinaryService cloudinaryService;
    private final PerformanceReviewRepository performanceReviewRepository;

    public LoginResdto login(LoginReqDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtUtil.generateToken(userDetails.getUsername(), userDetails.getAuthorities().iterator().next().getAuthority());

        return new LoginResdto("Login successful", token);
    }

    public void addEmployee(UserReqDto userReqDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User creator = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if (creator.getUserStatus()!= UserStatus.ACTIVE){
            throw new InActiveUserException("Your account is not active");

        }
        Role role = roleRepository.findByRoleName(userReqDto.getRole()).orElseThrow(() -> new EmailAlreadyExistException("Role not found"));

        Department department = departmentRepository.findById(userReqDto.getDepartmentId()).orElseThrow(() -> new EmailAlreadyExistException("Email already exist"));

        if (userRepository.existsByEmail(userReqDto.getEmail())){
            throw new EmailAlreadyExistException("User already exist");
        }

        String imageUrl = null;
        if (userReqDto.getProfilePhoto() != null && !userReqDto.getProfilePhoto().isEmpty()) {
            try {
                imageUrl = cloudinaryService.uploadFile(userReqDto.getProfilePhoto());
            } catch (IOException e) {
                throw new RuntimeException("Error uploading image to Cloudinary", e);
            }
        }
        User user = new User();
        user.setEmail(userReqDto.getEmail());
        user.setPassword(userReqDto.getPassword());
        user.setSalary(userReqDto.getSalary());
        user.setDesignation(userReqDto.getDesignation());
        user.setUsername(userReqDto.getName());
        user.setRole(role);
        user.setProfilePhoto(imageUrl);
        user.setDepartment(department);
        user.setUserStatus(UserStatus.ACTIVE);

        userRepository.save(user);
    }


    public List<GetUserResponseDto> allEmployees() {
        Role role = roleRepository.findByRoleName(RoleName.EMPLOYEE)
                .orElseThrow(() -> new ElementNotFoundExceprion("Role not found"));

        List<User> allEmployee = userRepository.findByRole(role);
        List<GetUserResponseDto> userResponseDtoList = new ArrayList<>();

        for (User user : allEmployee) {
            GetUserResponseDto userResponseDto = new GetUserResponseDto();
            userResponseDto.setUserId(user.getUserId());
            userResponseDto.setUserName(user.getUsername());
            userResponseDto.setEmail(user.getEmail());
            userResponseDto.setDesignation(user.getDesignation());
            userResponseDto.setProfilePhoto(user.getProfilePhoto());
            userResponseDto.setRole(user.getRole().getRoleName().getValue());

            userResponseDtoList.add(userResponseDto);
        }
        return userResponseDtoList;
    }




}
