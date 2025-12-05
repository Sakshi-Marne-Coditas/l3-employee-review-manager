package com.l3ProblemStatement.repository;

import com.l3ProblemStatement.entity.EmployeeDocs;
import com.l3ProblemStatement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeDocsRepository extends JpaRepository<EmployeeDocs, Long> {
    Optional<EmployeeDocs> findByEmployee(User employee);
}

