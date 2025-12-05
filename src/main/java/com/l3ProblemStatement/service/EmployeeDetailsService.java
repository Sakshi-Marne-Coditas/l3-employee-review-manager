package com.l3ProblemStatement.service;

import com.l3ProblemStatement.constants.RoleName;
import com.l3ProblemStatement.constants.UserStatus;
import com.l3ProblemStatement.dto.*;
import com.l3ProblemStatement.entity.EmployeeDocs;
import com.l3ProblemStatement.entity.User;
import com.l3ProblemStatement.entity.UserCertificate;
import com.l3ProblemStatement.exceptionHandling.InActiveUserException;
import com.l3ProblemStatement.repository.EmployeeCertificateRepository;
import com.l3ProblemStatement.repository.EmployeeDocsRepository;
import com.l3ProblemStatement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeDetailsService {
    private final UserRepository userRepository;
    private final EmployeeDocsRepository employeeDocsRepository;
    private final CloudinaryService cloudinaryService;
    private final EmployeeCertificateRepository certificateRepository;

    public EmployeeDocsResponseDto uploadEmployeeDocs(EmployeeDocsUploadDto uploadDto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        User targetEmployee;
        if (uploadDto.getEmployeeId() != null) {
            if (loggedInUser.getRole().getRoleName() != RoleName.ADMIN) {
                throw new AccessDeniedException("Only admin can upload documents for another employee");
            }
            targetEmployee = userRepository.findById(uploadDto.getEmployeeId()).orElseThrow(() -> new UsernameNotFoundException("Employee not found"));

        } else {
            targetEmployee = loggedInUser;
        }
        EmployeeDocs docs = employeeDocsRepository
                .findByEmployee(targetEmployee)
                .orElse(new EmployeeDocs());

        docs.setEmployee(targetEmployee);
        docs.setCreatedBy(loggedInUser);

        if (uploadDto.getResume() != null && !uploadDto.getResume().isEmpty()) {
            try {
                String resumeUrl = cloudinaryService.uploadFile(uploadDto.getResume());
                docs.setResume(resumeUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error uploading resume to Cloudinary", e);
            }
        }

        EmployeeDocs savedDocs = employeeDocsRepository.save(docs);
        List<CertificateResponseDto> savedCertificates = new ArrayList<>();

        if (uploadDto.getCertificates() != null && !uploadDto.getCertificates().isEmpty()) {

            for (CertificateUploadDto certDto : uploadDto.getCertificates()) {

                if (certDto.getCertificateFile() == null ||
                        certDto.getCertificateFile().isEmpty()) {
                    continue;
                }

                String certificateUrl;
                try {
                    certificateUrl = cloudinaryService.uploadFile(
                            certDto.getCertificateFile()
                    );
                } catch (IOException e) {
                    throw new RuntimeException("Error uploading certificate to Cloudinary", e);
                }

                UserCertificate certificate = new UserCertificate();
                certificate.setCertificateName(certDto.getCertificateName());
                certificate.setCertificateImage(certificateUrl);
                certificate.setEmployeeDocs(savedDocs);
                UserCertificate savedCert = certificateRepository.save(certificate);
                savedCertificates.add(
                        new CertificateResponseDto(
                                savedCert.getCertificateId(),
                                savedCert.getCertificateName(),
                                savedCert.getCertificateImage()
                        )
                );
            }
        }
        EmployeeDocsResponseDto response = new EmployeeDocsResponseDto();
        response.setDocsId(savedDocs.getId());
        response.setResumeUrl(savedDocs.getResume());
        response.setCertificates(savedCertificates);

        return response;
    }

    public EmployeeDocsResponseDto getMyDocs() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        EmployeeDocs docs = employeeDocsRepository.findByEmployee(loggedInUser).orElseThrow(() -> new EntityNotFoundException("No documents found for this user"));
        List<CertificateResponseDto> certificateDtos = new ArrayList<>();

        if (docs.getCertificates() != null && !docs.getCertificates().isEmpty()) {
            for (UserCertificate certificate : docs.getCertificates()) {
                certificateDtos.add(new CertificateResponseDto(certificate.getCertificateId(), certificate.getCertificateName(), certificate.getCertificateImage()));
            }
        }

        EmployeeDocsResponseDto response = new EmployeeDocsResponseDto();
        response.setDocsId(docs.getId());
        response.setResumeUrl(docs.getResume());
        response.setCertificates(certificateDtos);

        return response;
    }

    public EmployeeDocsResponseDto updateEmployeeDocs(EmployeeDocsUpdateDto uploadDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userRepository.findByEmail(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (loggedInUser.getUserStatus() != UserStatus.ACTIVE) {
            throw new InActiveUserException("Your account is not active");
        }
        User targetEmployee;
        if (uploadDto.getEmployeeId() != null) {
            if (loggedInUser.getRole().getRoleName() != RoleName.ADMIN) {
                throw new AccessDeniedException("Only admin can update other employee docs");
            }
            targetEmployee = userRepository.findById(uploadDto.getEmployeeId()).orElseThrow(() -> new UsernameNotFoundException("Employee not found"));
        }
        else {
            targetEmployee = loggedInUser;
        }
        EmployeeDocs docs = employeeDocsRepository.findByEmployee(targetEmployee).orElseThrow(() -> new EntityNotFoundException("Employee docs not found"));
        docs.setUpdatedAt(LocalDateTime.now());
        if (uploadDto.getResume() != null && !uploadDto.getResume().isEmpty()) {
            try {
                String resumeUrl = cloudinaryService.uploadFile(uploadDto.getResume());
                docs.setResume(resumeUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error uploading resume", e);
            }
        }

        EmployeeDocs savedDocs = employeeDocsRepository.save(docs);
        List<CertificateResponseDto> certificates = new ArrayList<>();

        if (uploadDto.getCertificates() != null && !uploadDto.getCertificates().isEmpty()) {

            for (CertificateUploadDto certDto : uploadDto.getCertificates()) {

                if (certDto.getCertificateFile() == null || certDto.getCertificateFile().isEmpty()) {
                    continue;
                }

                String certUrl;
                try {
                    certUrl = cloudinaryService.uploadFile(certDto.getCertificateFile());
                } catch (IOException e) {
                    throw new RuntimeException("Error uploading certificate", e);
                }

                UserCertificate certificate = new UserCertificate();
                certificate.setCertificateName(certDto.getCertificateName());
                certificate.setCertificateImage(certUrl);
                certificate.setEmployeeDocs(savedDocs);

                UserCertificate savedCert = certificateRepository.save(certificate);

                certificates.add(new CertificateResponseDto(
                        savedCert.getCertificateId(),
                        savedCert.getCertificateName(),
                        savedCert.getCertificateImage()
                ));
            }
        }
        EmployeeDocsResponseDto response = new EmployeeDocsResponseDto();
        response.setDocsId(savedDocs.getId());
        response.setResumeUrl(savedDocs.getResume());
        response.setCertificates(certificates);

        return response;
    }



}
