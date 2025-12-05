package com.l3ProblemStatement.controller;

import com.l3ProblemStatement.dto.*;
import com.l3ProblemStatement.service.EmployeeDetailsService;
import com.l3ProblemStatement.service.PerformanceReviewService;
import com.l3ProblemStatement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class UserController {
    private final UserService userService;
    private final PerformanceReviewService reviewService;
    private final EmployeeDetailsService employeeDetailsService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/new-employee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> addEmployee(@Valid @ModelAttribute UserReqDto userReqDto){
        userService.addEmployee(userReqDto);

        ApiResponse<Void> response = new ApiResponse("success",true,null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','MANAGER')")
    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> updateUser(@RequestParam(required = false) Long userId, @Valid @ModelAttribute UpdateUserdto userReqDto) {
        userService.updateUser(userId, userReqDto);
        ApiResponse<Void> response = new ApiResponse<>("Profile updated successfully", true, null);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@RequestParam Long userId) {
        userService.deactivateUser(userId);
        ApiResponse<Void> response = new ApiResponse<>("User deactivated successfully", true, null);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<GetUserResponseDto>>> getEmployees(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "joiningDate") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Page<GetUserResponseDto> result = userService.getAllEmployees(search, departmentId, role, page, size, sortBy, sortDir);
        ApiResponse<Page<GetUserResponseDto>> response = new ApiResponse<>("success", true, result);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/review")
    public ResponseEntity<ApiResponse<Void>> addReview(@Valid @RequestBody ReviewReqDto reviewReqDto){
        reviewService.addReview(reviewReqDto);

        ApiResponse<Void> response = new ApiResponse("review submitted successfully",true,null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    @GetMapping("/my-review")
    public ResponseEntity<ApiResponse<Page<ReviewResdto>>> myReview(
            @RequestParam(required = false) Long employeeId,
            @RequestParam(required = false) Integer rating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ReviewResdto> responseDto = reviewService.myReview(employeeId, rating, page, size);
        ApiResponse<Page<ReviewResdto>> response = new ApiResponse<>("Reviews fetched successfully", true, responseDto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PutMapping("/update-review/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> updateReview(@PathVariable Long reviewId, @Valid @RequestBody UpdateReviewReqDto reviewReqDto) {
        reviewService.updateReview(reviewId, reviewReqDto);
        ApiResponse<Void> response = new ApiResponse<>("Review updated successfully", true, null);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PostMapping("/docs-upload")
    public ResponseEntity<ApiResponse<EmployeeDocsResponseDto>> uploadEmployeeDocs(@ModelAttribute EmployeeDocsUploadDto uploadDto) {
        EmployeeDocsResponseDto response = employeeDetailsService.uploadEmployeeDocs(uploadDto);
        ApiResponse<EmployeeDocsResponseDto> apiResponse = new ApiResponse<>("Documents uploaded successfully", true, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @PutMapping(value = "/docs-update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<EmployeeDocsResponseDto>> updateEmployeeDocs(@ModelAttribute EmployeeDocsUpdateDto uploadDto) {
        EmployeeDocsResponseDto response = employeeDetailsService.updateEmployeeDocs(uploadDto);
        ApiResponse<EmployeeDocsResponseDto> apiResponse = new ApiResponse<>("Documents updated successfully", true, response);
        return ResponseEntity.ok(apiResponse);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/my-docs")
    public ResponseEntity<ApiResponse<EmployeeDocsResponseDto>> getMyDocs() {
        EmployeeDocsResponseDto responseDto = employeeDetailsService.getMyDocs();
        ApiResponse<EmployeeDocsResponseDto> response = new ApiResponse<>("Documents fetched successfully", true, responseDto);
        return ResponseEntity.ok(response);
    }



}
