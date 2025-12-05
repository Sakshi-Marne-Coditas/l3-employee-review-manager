package com.l3ProblemStatement.service;

import com.l3ProblemStatement.constants.RoleName;
import com.l3ProblemStatement.constants.UserStatus;
import com.l3ProblemStatement.dto.CreateDepartmentReqDto;
import com.l3ProblemStatement.entity.Department;
import com.l3ProblemStatement.entity.User;
import com.l3ProblemStatement.exceptionHandling.InActiveUserException;
import com.l3ProblemStatement.repository.DepartmentRepository;
import com.l3ProblemStatement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DepartmentService {
   private final UserRepository userRepository;
   private final DepartmentRepository departmentRepository;
    public void createDepartment(CreateDepartmentReqDto dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User admin = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (admin.getUserStatus() != UserStatus.ACTIVE) {
            throw new InActiveUserException("Your account is not active");
        }
        if (admin.getRole().getRoleName() != RoleName.ADMIN) {
            throw new AccessDeniedException("Only admin can create department");
        }
        if (departmentRepository.existsByNameIgnoreCase(dto.getName())) {
            throw new EntityAl("Department already exists");
        }
        Department department = new Department();
        department.setName(dto.getName());

        departmentRepository.save(department);
    }

}
