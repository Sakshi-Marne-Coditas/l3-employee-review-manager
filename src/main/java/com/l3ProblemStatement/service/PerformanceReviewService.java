package com.l3ProblemStatement.service;

import com.l3ProblemStatement.constants.RoleName;
import com.l3ProblemStatement.constants.UserStatus;
import com.l3ProblemStatement.dto.ReviewReqDto;
import com.l3ProblemStatement.dto.ReviewResdto;
import com.l3ProblemStatement.dto.UpdateReviewReqDto;
import com.l3ProblemStatement.entity.PerformanceReview;
import com.l3ProblemStatement.entity.User;
import com.l3ProblemStatement.exceptionHandling.ElementNotFoundExceprion;
import com.l3ProblemStatement.exceptionHandling.InActiveUserException;
import com.l3ProblemStatement.repository.PerformanceReviewRepository;
import com.l3ProblemStatement.repository.UserRepository;
import com.l3ProblemStatement.specification.ReviewSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceReviewService {
    private final UserRepository userRepository;
    private final PerformanceReviewRepository performanceReviewRepository;

        public void addReview(ReviewReqDto reviewReqDto) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User manager = userRepository.findByEmail(authentication.getName())
                    .orElseThrow(() -> new UsernameNotFoundException("Manager not found"));
            if (manager.getUserStatus()!= UserStatus.ACTIVE){
                throw new InActiveUserException("Your account is not active");

            }
            User employee = userRepository.findById(reviewReqDto.getEmployeeId()).orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

            if (!employee.getManager().getUserId().equals(manager.getUserId())) {
                throw new AccessDeniedException("You are not the manager of this employee");
            }

            PerformanceReview review = new PerformanceReview();
            review.setRating(reviewReqDto.getPoints());
            review.setComment(reviewReqDto.getComment());
            review.setEmployee(employee);
            review.setCreatedBy(manager);

            performanceReviewRepository.save(review);
        }

    public void updateReview(Long reviewId, UpdateReviewReqDto reviewReqDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (loggedInUser.getUserStatus() != UserStatus.ACTIVE) {
            throw new InActiveUserException("Your account is not active");
        }
        PerformanceReview review = performanceReviewRepository.findById(reviewId).orElseThrow(() -> new EntityNotFoundException("Review not found"));
        if (!review.getCreatedBy().getUserId().equals(loggedInUser.getUserId())) {
            throw new AccessDeniedException("You are not allowed to update this review");
        }
            review.setRating(reviewReqDto.getPoints());
            review.setComment(reviewReqDto.getComment());

        performanceReviewRepository.save(review);
    }

    public Page<ReviewResdto> myReview(Long employeeId, Integer rating, int page, int size) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        if (loggedInUser.getUserStatus() != UserStatus.ACTIVE) {
            throw new InActiveUserException("Your account is not active");
        }
        User targetEmployee;

        if (employeeId!=null) {
            if (loggedInUser.getRole().getRoleName() != RoleName.ADMIN && loggedInUser.getRole().getRoleName() != RoleName.MANAGER) {
                throw new AccessDeniedException("You are not allowed to view other employee reviews");
            }
            targetEmployee = userRepository.findById(employeeId).orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

        } else {
            targetEmployee = loggedInUser;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Specification<PerformanceReview> spec = ReviewSpecification.byEmployee(targetEmployee);

        if (rating != null) {
            spec = spec.and(ReviewSpecification.byRating(rating));
        }
        Page<PerformanceReview> reviews = performanceReviewRepository.findAll(spec, pageable);
        return reviews.map(r -> {
            ReviewResdto dto = new ReviewResdto();
            dto.setGivenBy(r.getCreatedBy().getUsername());
            dto.setRating(r.getRating());
            dto.setComment(r.getComment());
            return dto;
        });
    }


}
