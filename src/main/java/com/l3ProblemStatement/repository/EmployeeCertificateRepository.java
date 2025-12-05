package com.l3ProblemStatement.repository;

import com.l3ProblemStatement.entity.UserCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeCertificateRepository extends JpaRepository<UserCertificate, Long> {
}

