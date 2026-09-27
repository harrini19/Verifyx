package com.certificate.verification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Service responsible for all cryptographic hashing operations.
 *
 * SHA-256 (Secure Hash Algorithm 256-bit) produces a deterministic 256-bit (64 hex character)
 * fingerprint from any input data. The same input ALWAYS produces the same hash.
 * Even a single character change produces a completely different hash.
 *
 * This tamper-evident property is used to verify certificate integrity:
 * if the certificate data has not been tampered with, recomputing the hash from the
 * stored data will always match the originally stored hash.
 *
 * Uses Java standard library (java.security.MessageDigest) — no external libraries required.
 */
@Service
@Slf4j
public class HashService {

    private static final String ALGORITHM = "SHA-256";
    private static final String GENESIS_HASH = "GENESIS";

    /**
     * Generates a SHA-256 hash from the given input string.
     *
     * @param input the data to hash
     * @return lowercase hexadecimal SHA-256 hash string (64 characters)
     * @throws RuntimeException if SHA-256 algorithm is not available (should never happen on JVM)
     */
    public String generateSha256Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed to be available on all Java SE implementations
            log.error("SHA-256 algorithm not available: {}", e.getMessage());
            throw new RuntimeException("Hashing service unavailable", e);
        }
    }

    /**
     * Builds the canonical string for a certificate before hashing.
     * Format: certificateNumber|studentId|studentName|institution|course|grade|issueDate
     *
     * This canonical format ensures the hash is deterministic and reproducible.
     * Any tampering with any field will produce a different hash.
     *
     * @return canonical pipe-delimited string
     */
    public String buildCertificateCanonical(String certificateNumber, String studentId,
                                            String studentName, String institution,
                                            String course, String grade, String issueDate) {
        return String.join("|",
                certificateNumber.trim(),
                studentId.trim(),
                studentName.trim(),
                institution.trim(),
                course.trim(),
                grade.trim(),
                issueDate.trim()
        );
    }

    /**
     * Builds a ledger entry hash from the certificate hash, previous hash, and timestamp.
     * This creates the tamper-evident chain between ledger entries.
     */
    public String buildLedgerHash(String certificateHash, String previousHash, String timestamp) {
        String input = certificateHash + "|" + previousHash + "|" + timestamp;
        return generateSha256Hash(input);
    }

    /**
     * Returns the genesis hash sentinel value used for the first ledger entry.
     */
    public String getGenesisHash() {
        return GENESIS_HASH;
    }

    /**
     * Converts a byte array to a lowercase hexadecimal string.
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
