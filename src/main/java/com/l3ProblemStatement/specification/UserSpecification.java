package com.l3ProblemStatement.specification;

import com.l3ProblemStatement.constants.UserStatus;
import com.l3ProblemStatement.entity.Role;
import com.l3ProblemStatement.entity.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    public static Specification<User> hasNameOrEmail(String search) {

        return (root, query, cb) -> {

            if (search == null || search.isBlank()) {
                return null;
            }

            String likePattern = "%" + search.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("username")), likePattern),
                    cb.like(cb.lower(root.get("email")), likePattern)
            );
        };
    }

    public static Specification<User> hasDepartment(Long departmentId) {
        return (root, query, cb) -> {

            if (departmentId == null) return null;

            return cb.equal(root.get("department").get("departmentId"), departmentId);
        };
    }


    public static Specification<User> hasRole(Role role) {
        return (root, query, cb) -> {

            if (role == null) {
                return null;
            }
            return cb.equal(root.get("role"), role);
        };
    }

    public static Specification<User> isActive() {
        return (root, query, cb) ->
                cb.equal(root.get("userStatus"), UserStatus.ACTIVE);
    }
}
