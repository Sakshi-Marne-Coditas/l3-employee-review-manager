package com.l3ProblemStatement.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificateId;

    private String certificateName;
    private String certificateImage;

    @ManyToOne
    @JoinColumn(name = "employee_docs_id")
    private EmployeeDocs employeeDocs;
}

