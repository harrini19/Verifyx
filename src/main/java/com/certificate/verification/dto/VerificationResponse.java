package com.certificate.verification.dto;

import lombok.*;

import java.time.LocalDate;

/**
 * Response DTO for certificate verification requests.
 * Returns full certificate details if verified, or a failure message if not found.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationResponse {

    private boolean verified;
    private String status;
    private String message;

    // Certificate details (populated when verified = true)
    private String certificateNumber;
    private String studentName;
    private String studentId;
    private String institution;
    private String course;
    private String grade;
    private LocalDate issueDate;
    private String issuer;
    private String sha256Hash;
}
