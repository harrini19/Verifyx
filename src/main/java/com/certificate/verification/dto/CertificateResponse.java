package com.certificate.verification.dto;

import com.certificate.verification.model.CertificateStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Response DTO returned after issuing a certificate.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateResponse {

    private Long certificateId;
    private String certificateNumber;
    private String studentName;
    private String studentId;
    private String institution;
    private String course;
    private String grade;
    private LocalDate issueDate;
    private String issuer;
    private String certificateData;
    private String sha256Hash;
    private CertificateStatus status;
    private LocalDateTime createdAt;
}
