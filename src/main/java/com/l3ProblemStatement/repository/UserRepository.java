package com.l3ProblemStatement.repository;

import com.l3ProblemStatement.constants.RoleName;
import com.l3ProblemStatement.constants.UserStatus;
import com.l3ProblemStatement.entity.Role;
import com.l3ProblemStatement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {



    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findByUserStatus(UserStatus userStatus);

    List<User> findByRole(Role role);
}
