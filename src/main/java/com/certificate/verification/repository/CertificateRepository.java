package com.certificate.verification.repository;

import com.certificate.verification.model.Certificate;
import com.certificate.verification.model.CertificateStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    Optional<Certificate> findBySha256Hash(String sha256Hash);

    Optional<Certificate> findByCertificateNumber(String certificateNumber);

    boolean existsByCertificateNumber(String certificateNumber);

    List<Certificate> findByStatus(CertificateStatus status);

    List<Certificate> findByStudentNameContainingIgnoreCaseOrStudentIdContainingIgnoreCase(
            String name, String studentId);

    long countByStatus(CertificateStatus status);
}
