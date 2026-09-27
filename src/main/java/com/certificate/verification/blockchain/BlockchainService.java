package com.certificate.verification.blockchain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Simulated blockchain utility service.
 *
 * <p>In production this would talk to an Ethereum node via Web3j or a
 * Hyperledger Fabric gateway.  Here we implement the same cryptographic
 * primitives (SHA-256 hashing) that a real smart-contract would use, so the
 * integrity guarantees are identical — only the distributed consensus and
 * immutability of the ledger are simulated in-memory.</p>
 */
@Slf4j
@Component
public class BlockchainService {

    /**
     * Computes the canonical SHA-256 fingerprint for a certificate.
     *
     * <p>The canonical string is deterministic:
     * {@code certificateNumber|studentId|studentName|institution|course|grade|issueDate}</p>
     *
     * @param canonicalData pre-built canonical string from the certificate
     * @return 64-character lower-case hex digest
     */
    public String computeHash(String canonicalData) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(canonicalData.getBytes(StandardCharsets.UTF_8));
            String hash = HexFormat.of().formatHex(hashBytes);
            log.debug("Computed hash for canonical data: {}", hash);
            return hash;
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed to be available in every JVM
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Builds the canonical data string from individual certificate fields.
     *
     * @param certificateNumber unique certificate number
     * @param studentId         student identifier
     * @param studentName       full student name
     * @param institution       issuing institution
     * @param course            course / degree title
     * @param grade             grade / result
     * @param issueDate         issue date as ISO-8601 string (yyyy-MM-dd)
     * @return pipe-delimited canonical string
     */
    public String buildCanonicalData(String certificateNumber, String studentId,
                                     String studentName, String institution,
                                     String course, String grade, String issueDate) {
        return String.join("|",
                certificateNumber, studentId, studentName, institution, course, grade, issueDate);
    }

    /**
     * Verifies that a given hash matches the recomputed hash of the supplied
     * canonical data.  This simulates querying the blockchain ledger.
     *
     * @param canonicalData the certificate's canonical data string
     * @param storedHash    the hash that was previously recorded
     * @return {@code true} if the certificate is authentic and untampered
     */
    public boolean verifyHash(String canonicalData, String storedHash) {
        String recomputed = computeHash(canonicalData);
        boolean match = recomputed.equalsIgnoreCase(storedHash);
        if (match) {
            log.info("Certificate integrity verified — hash matches.");
        } else {
            log.warn("Certificate integrity FAILED — hash mismatch! Stored={}, Computed={}",
                    storedHash, recomputed);
        }
        return match;
    }
}
