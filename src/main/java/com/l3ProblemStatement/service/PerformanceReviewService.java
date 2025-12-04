package com.l3ProblemStatement.service;

import com.l3ProblemStatement.dto.ApiResponse;
import com.l3ProblemStatement.dto.ReviewReqDto;
import com.l3ProblemStatement.entity.PerformanceReview;
import com.l3ProblemStatement.entity.User;
import com.l3ProblemStatement.repository.PerformanceReviewRepository;
import com.l3ProblemStatement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@RequiredArgsConstructor
public class PerformanceReviewService {
    UserRepository userRepository;
    PerformanceReviewRepository performanceReviewRepository;
    public void addReview(ReviewReqDto reviewReqDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User createdBy = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("Manager not found"));
        PerformanceReview performanceReview = new PerformanceReview();
        performanceReview.setRating(reviewReqDto.getPoints());
        performanceReview.setComment(reviewReqDto.getComment());
        performanceReview.setCreatedBy(createdBy);
        performanceReviewRepository.save(performanceReview);

    }

    public void myReview(ReviewReqDto reviewReqDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User employee = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        PerformanceReview review = performanceReviewRepository.findByEmployee(employee);
        
    }
}
