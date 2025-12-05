package com.l3ProblemStatement.specification;

import com.l3ProblemStatement.entity.PerformanceReview;
import com.l3ProblemStatement.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecification {

    public static Specification<PerformanceReview> byEmployee(User employee) {
        return (root, query, cb) -> cb.equal(root.get("employee"), employee);
    }

    public static Specification<PerformanceReview> byRating(Integer rating) {
        return (root, query, cb) -> rating == null
                ? null
                : cb.equal(root.get("rating"), rating);
    }
}

