package com.certificate.verification.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a digital academic certificate issued by an institution.
 * The SHA-256 hash is derived deterministically from the certificate's canonical data
 * and serves as the tamper-evident fingerprint for verification.
 */
@Entity
@Table(name = "certificates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long certificateId;

    @Column(nullable = false, unique = true, length = 50)
    private String certificateNumber;

    @Column(nullable = false, length = 150)
    private String studentName;

    @Column(nullable = false, length = 50)
    private String studentId;

    @Column(nullable = false, length = 200)
    private String institution;

    @Column(nullable = false, length = 200)
    private String course;

    @Column(nullable = false, length = 10)
    private String grade;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false, length = 150)
    private String issuer;

    /**
     * Canonical string used as input for SHA-256 hashing.
     * Format: certificateNumber|studentId|studentName|institution|course|grade|issueDate
     */
    @Column(nullable = false, length = 1000)
    private String certificateData;

    /**
     * SHA-256 hexadecimal hash of the certificateData.
     * Used as the unique tamper-evident fingerprint for verification.
     */
    @Column(nullable = false, unique = true, length = 64)
    private String sha256Hash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificateStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = CertificateStatus.VALID;
        }
    }
}
