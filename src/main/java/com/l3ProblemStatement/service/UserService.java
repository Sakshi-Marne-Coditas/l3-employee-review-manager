package com.l3ProblemStatement.service;

import com.l3ProblemStatement.constants.RoleName;
import com.l3ProblemStatement.constants.UserStatus;
import com.l3ProblemStatement.dto.*;
import com.l3ProblemStatement.entity.Department;
import com.l3ProblemStatement.entity.Role;
import com.l3ProblemStatement.entity.User;
import com.l3ProblemStatement.exceptionHandling.BadRequestException;
import com.l3ProblemStatement.exceptionHandling.EmailAlreadyExistException;
import com.l3ProblemStatement.exceptionHandling.InActiveUserException;
import com.l3ProblemStatement.repository.DepartmentRepository;
import com.l3ProblemStatement.repository.PerformanceReviewRepository;
import com.l3ProblemStatement.repository.RoleRepository;
import com.l3ProblemStatement.repository.UserRepository;
import com.l3ProblemStatement.security.CustomUserDetails;
import com.l3ProblemStatement.security.CustomUserDetailsService;
import com.l3ProblemStatement.security.JwtUtil;
import com.l3ProblemStatement.specification.UserSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

    public void updateUser(Long userId, UpdateUserdto userReqDto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (loggedInUser.getUserStatus() != UserStatus.ACTIVE) {
            throw new InActiveUserException("Your account is not active");
        }

        User targetUser;
        if (userId != null) {
            if (loggedInUser.getRole().getRoleName() != RoleName.ADMIN) {
                throw new AccessDeniedException("Only admin can update other users");
            }
            targetUser = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("Target user not found"));

        }
        else {
            targetUser = loggedInUser;
        }
        if (userReqDto.getEmail()!=null && !targetUser.getEmail().equals(userReqDto.getEmail())) {
            if (userRepository.existsByEmail(userReqDto.getEmail())) {
                throw new EmailAlreadyExistException("Email already exists");
            }
            targetUser.setEmail(userReqDto.getEmail());
        }
        if (userReqDto.getName() != null)
            targetUser.setUsername(userReqDto.getName());
        if (userReqDto.getSalary() != null)
            targetUser.setSalary(userReqDto.getSalary());

        if (userReqDto.getDesignation() != null)
            targetUser.setDesignation(userReqDto.getDesignation());
        if (loggedInUser.getRole().getRoleName() == RoleName.ADMIN) {
            if (userReqDto.getRole() != null) {
                Role role = roleRepository.findByRoleName(userReqDto.getRole()).orElseThrow(() -> new EntityNotFoundException("Role not found"));
                targetUser.setRole(role);
            }
            if (userReqDto.getDepartmentId() != null) {
                Department dept = departmentRepository.findById(userReqDto.getDepartmentId())
                        .orElseThrow(() -> new EntityNotFoundException("Department not found"));
                targetUser.setDepartment(dept);
            }
        }
        if (userReqDto.getProfilePhoto() != null && !userReqDto.getProfilePhoto().isEmpty()) {
            try {
                String imageUrl = cloudinaryService.uploadFile(userReqDto.getProfilePhoto());
                targetUser.setProfilePhoto(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error uploading image", e);
            }
        }

        userRepository.save(targetUser);
    }

    public void deactivateUser(Long userId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User admin = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
        if (admin.getUserStatus() != UserStatus.ACTIVE) {
            throw new InActiveUserException("Your account is not active");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.getUserStatus() == UserStatus.INACTIVE) {
            throw new IllegalStateException("User already deactivated");
        }
        user.setUserStatus(UserStatus.INACTIVE);
        userRepository.save(user);
    }


    public Page<GetUserResponseDto> getAllEmployees(String search, Long departmentId, String roleName, int page, int size, String sortBy, String sortDir
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if (loggedInUser.getUserStatus()!= UserStatus.ACTIVE){
            throw new InActiveUserException("Your account is not active");

        }
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Role role = null;
        try {
             role = roleRepository.findByRoleName(RoleName.valueOf(roleName)).orElseThrow(() -> new EntityNotFoundException("Role not found in database"));
        }catch (IllegalArgumentException e){
            throw new BadRequestException("Invalid role");
        }

        Specification<User> spec = null;

        if (search != null && !search.isBlank()) {
            spec = UserSpecification.hasNameOrEmail(search);
        }

        if (departmentId != null) {
            spec = (spec == null) ? UserSpecification.hasDepartment(departmentId) : spec.and(UserSpecification.hasDepartment(departmentId));
        }
        if (role != null) {
            spec = (spec == null) ? UserSpecification.hasRole(role) : spec.and(UserSpecification.hasRole(role));
        }
        spec = (spec == null) ? UserSpecification.isActive() : spec.and(UserSpecification.isActive());

        Page<User> users = userRepository.findAll(spec, pageable);

        return users.map(user -> {
            GetUserResponseDto dto = new GetUserResponseDto();

            dto.setUserId(user.getUserId());
            dto.setUserName(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setDesignation(user.getDesignation());
            dto.setProfilePhoto(user.getProfilePhoto());
            dto.setRole(user.getRole().getRoleName().name());

            return dto;
        });
    }


}
