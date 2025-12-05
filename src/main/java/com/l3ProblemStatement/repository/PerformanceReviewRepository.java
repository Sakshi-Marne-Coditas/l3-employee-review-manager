package com.l3ProblemStatement.repository;

import com.l3ProblemStatement.entity.PerformanceReview;
import com.l3ProblemStatement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceReviewRepository
        extends JpaRepository<PerformanceReview, Long>, JpaSpecificationExecutor<PerformanceReview> {
    PerformanceReview findByEmployee(User user);
}
