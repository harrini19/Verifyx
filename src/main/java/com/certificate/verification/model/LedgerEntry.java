package com.certificate.verification.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a ledger entry in the blockchain-inspired tamper-evident chain.
 *
 * Each certificate issued creates a ledger entry. The ledger entry contains:
 * - The certificate's hash (SHA-256 of certificate data)
 * - The previous entry's hash (forming a chain)
 * - Its own ledger hash (SHA-256 of: certificateHash + previousHash + timestamp)
 *
 * The first entry uses "GENESIS" as the previousHash, mimicking a blockchain genesis block.
 *
 * ACADEMIC NOTE: This is a simplified educational demonstration of hash-chaining concepts.
 * It is NOT a decentralized public blockchain network like Bitcoin or Ethereum.
 */
@Entity
@Table(name = "ledger_entries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ledgerId;

    @Column(nullable = false)
    private Long certificateId;

    @Column(nullable = false, length = 64)
    private String certificateHash;

    /**
     * Hash of the previous ledger entry. Set to "GENESIS" for the first entry.
     */
    @Column(nullable = false, length = 64)
    private String previousHash;

    /**
     * SHA-256 hash of: certificateHash + "|" + previousHash + "|" + timestamp
     */
    @Column(nullable = false, length = 64)
    private String ledgerHash;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}
