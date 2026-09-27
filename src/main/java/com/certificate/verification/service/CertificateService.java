package com.certificate.verification.service;

import com.certificate.verification.dto.CertificateRequest;
import com.certificate.verification.dto.CertificateResponse;
import com.certificate.verification.dto.VerificationResponse;
import com.certificate.verification.exception.DuplicateResourceException;
import com.certificate.verification.exception.ResourceNotFoundException;
import com.certificate.verification.model.Certificate;
import com.certificate.verification.model.CertificateStatus;
import com.certificate.verification.model.LedgerEntry;
import com.certificate.verification.repository.CertificateRepository;
import com.certificate.verification.repository.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for certificate issuance and verification operations.
 * Handles SHA-256 hash generation, ledger chain management, and certificate lifecycle.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CertificateService {

    private final CertificateRepository certificateRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final HashService hashService;

    /**
     * Issues a new digital certificate.
     * Steps:
     * 1. Validate uniqueness of certificate number
     * 2. Build canonical data string
     * 3. Generate SHA-256 hash from canonical data
     * 4. Persist certificate
     * 5. Create a ledger entry (blockchain-inspired chain)
     */
    @Transactional
    public CertificateResponse issueCertificate(CertificateRequest request) {
        if (certificateRepository.existsByCertificateNumber(request.getCertificateNumber())) {
            throw new DuplicateResourceException(
                    "Certificate number already exists: " + request.getCertificateNumber());
        }

        // Build canonical string for hashing
        String canonicalData = hashService.buildCertificateCanonical(
                request.getCertificateNumber(),
                request.getStudentId(),
                request.getStudentName(),
                request.getInstitution(),
                request.getCourse(),
                request.getGrade(),
                request.getIssueDate().toString()
        );

        // Generate SHA-256 hash — never trust client-supplied hash
        String sha256Hash = hashService.generateSha256Hash(canonicalData);

        Certificate certificate = Certificate.builder()
                .certificateNumber(request.getCertificateNumber())
                .studentName(request.getStudentName())
                .studentId(request.getStudentId())
                .institution(request.getInstitution())
                .course(request.getCourse())
                .grade(request.getGrade())
                .issueDate(request.getIssueDate())
                .issuer(request.getIssuer())
                .certificateData(canonicalData)
                .sha256Hash(sha256Hash)
                .status(CertificateStatus.VALID)
                .build();

        certificate = certificateRepository.save(certificate);
        log.info("Certificate issued: {} | Hash: {}", certificate.getCertificateNumber(), sha256Hash);

        // Create ledger entry for blockchain-inspired chain
        createLedgerEntry(certificate);

        return mapToResponse(certificate);
    }

    /**
     * Verifies a certificate by its SHA-256 hash.
     */
    @Transactional(readOnly = true)
    public VerificationResponse verifyCertificate(String hash) {
        return certificateRepository.findBySha256Hash(hash.trim().toLowerCase())
                .map(cert -> VerificationResponse.builder()
                        .verified(true)
                        .status(cert.getStatus().name())
                        .message("Certificate verified successfully")
                        .certificateNumber(cert.getCertificateNumber())
                        .studentName(cert.getStudentName())
                        .studentId(cert.getStudentId())
                        .institution(cert.getInstitution())
                        .course(cert.getCourse())
                        .grade(cert.getGrade())
                        .issueDate(cert.getIssueDate())
                        .issuer(cert.getIssuer())
                        .sha256Hash(cert.getSha256Hash())
                        .build())
                .orElseGet(() -> VerificationResponse.builder()
                        .verified(false)
                        .status("NOT_FOUND")
                        .message("Certificate could not be verified. The hash does not match any issued certificate.")
                        .build());
    }

    @Transactional(readOnly = true)
    public List<CertificateResponse> getAllCertificates() {
        return certificateRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CertificateResponse getCertificateById(Long id) {
        Certificate cert = certificateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Certificate", "id", id));
        return mapToResponse(cert);
    }

    public long countTotal() {
        return certificateRepository.count();
    }

    public long countValid() {
        return certificateRepository.countByStatus(CertificateStatus.VALID);
    }

    public List<Certificate> getRecentCertificates() {
        List<Certificate> all = certificateRepository.findAll();
        int size = all.size();
        return all.subList(Math.max(0, size - 5), size)
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());
    }

    /**
     * Creates a ledger entry for a newly issued certificate.
     * Links to previous entry hash to form a tamper-evident chain.
     */
    private void createLedgerEntry(Certificate certificate) {
        String previousHash = ledgerEntryRepository.findTopByOrderByLedgerIdDesc()
                .map(LedgerEntry::getLedgerHash)
                .orElse(hashService.getGenesisHash());

        String timestamp = LocalDateTime.now().toString();
        String ledgerHash = hashService.buildLedgerHash(
                certificate.getSha256Hash(), previousHash, timestamp);

        LedgerEntry entry = LedgerEntry.builder()
                .certificateId(certificate.getCertificateId())
                .certificateHash(certificate.getSha256Hash())
                .previousHash(previousHash)
                .ledgerHash(ledgerHash)
                .build();

        ledgerEntryRepository.save(entry);
        log.info("Ledger entry created for certificate ID: {}", certificate.getCertificateId());
    }

    private CertificateResponse mapToResponse(Certificate cert) {
        return CertificateResponse.builder()
                .certificateId(cert.getCertificateId())
                .certificateNumber(cert.getCertificateNumber())
                .studentName(cert.getStudentName())
                .studentId(cert.getStudentId())
                .institution(cert.getInstitution())
                .course(cert.getCourse())
                .grade(cert.getGrade())
                .issueDate(cert.getIssueDate())
                .issuer(cert.getIssuer())
                .certificateData(cert.getCertificateData())
                .sha256Hash(cert.getSha256Hash())
                .status(cert.getStatus())
                .createdAt(cert.getCreatedAt())
                .build();
    }
}
