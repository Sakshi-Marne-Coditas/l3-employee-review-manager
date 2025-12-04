package com.l3ProblemStatement.repository;

import com.l3ProblemStatement.entity.PerformanceReview;
import com.l3ProblemStatement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PerformanceReviewRepository extends JpaRepository<PerformanceReview, Long> {
    PerformanceReview findByEmployee(User user);
}
