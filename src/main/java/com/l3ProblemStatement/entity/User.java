package com.l3ProblemStatement.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.l3ProblemStatement.constants.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;

    private String email;

    private String password;

    private String designation;

    private double salary;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @JsonFormat(pattern = "dd/MM/yyyy")
     private LocalDate joiningDate;

     private String profilePhoto;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<PerformanceReview> reviews;

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<PerformanceReview> review;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doc_id")
    private EmployeeDocs employeeDocs;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @OneToMany(mappedBy = "manager")
    private List<User> employees;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;



}
