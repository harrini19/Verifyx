package com.certificate.verification.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Request DTO for issuing a new certificate.
 * The SHA-256 hash is generated on the backend; never supplied by the client.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateRequest {

    @NotBlank(message = "Certificate number is required")
    @Size(max = 50, message = "Certificate number must not exceed 50 characters")
    private String certificateNumber;

    @NotBlank(message = "Student name is required")
    @Size(max = 150, message = "Student name must not exceed 150 characters")
    private String studentName;

    @NotBlank(message = "Student ID is required")
    @Size(max = 50, message = "Student ID must not exceed 50 characters")
    private String studentId;

    @NotBlank(message = "Institution is required")
    @Size(max = 200, message = "Institution name must not exceed 200 characters")
    private String institution;

    @NotBlank(message = "Course is required")
    @Size(max = 200, message = "Course name must not exceed 200 characters")
    private String course;

    @NotBlank(message = "Grade is required")
    @Size(max = 10, message = "Grade must not exceed 10 characters")
    private String grade;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    @NotBlank(message = "Issuer is required")
    @Size(max = 150, message = "Issuer must not exceed 150 characters")
    private String issuer;
}
