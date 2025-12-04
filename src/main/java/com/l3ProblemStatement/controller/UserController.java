package com.l3ProblemStatement.controller;

import com.l3ProblemStatement.dto.ApiResponse;
import com.l3ProblemStatement.dto.GetUserResponseDto;
import com.l3ProblemStatement.dto.ReviewReqDto;
import com.l3ProblemStatement.dto.UserReqDto;
import com.l3ProblemStatement.service.PerformanceReviewService;
import com.l3ProblemStatement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class UserController {
    private final UserService userService;
    private final PerformanceReviewService reviewService;

    @PostMapping(value = "/new-employee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<Void>> addEmployee(@ModelAttribute UserReqDto userReqDto){
        userService.addEmployee(userReqDto);

        ApiResponse<Void> response = new ApiResponse("success",true,null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<GetUserResponseDto>>> allEmployees(){
        List<GetUserResponseDto> response= userService.allEmployees();
        ApiResponse<List<GetUserResponseDto>> userApiResponse = new ApiResponse<>("sucess",true, response);
        return ResponseEntity.ok(userApiResponse);
    }

    @PostMapping("/review")
    public ResponseEntity<ApiResponse<Void>> addReview(ReviewReqDto reviewReqDto){
        reviewService.addReview(reviewReqDto);

        ApiResponse<Void> response = new ApiResponse("review submitted successfully",true,null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping("/my-review")
    public ResponseEntity<ApiResponse<Void>> myReview( ReviewReqDto reviewReqDto){
        reviewService.myReview(reviewReqDto);

        ApiResponse<Void> response = new ApiResponse("review submitted successfully",true,null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }
}
