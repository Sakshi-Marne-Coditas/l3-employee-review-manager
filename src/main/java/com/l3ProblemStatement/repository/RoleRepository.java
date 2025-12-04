package com.l3ProblemStatement.repository;

import com.l3ProblemStatement.constants.RoleName;
import com.l3ProblemStatement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleName roleName);
}
