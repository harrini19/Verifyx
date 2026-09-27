package com.certificate.verification.repository;

import com.certificate.verification.model.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {

    Optional<LedgerEntry> findByCertificateId(Long certificateId);

    Optional<LedgerEntry> findTopByOrderByLedgerIdDesc();

    List<LedgerEntry> findAllByOrderByLedgerIdAsc();
}
