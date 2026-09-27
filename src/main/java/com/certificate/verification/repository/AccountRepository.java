package com.certificate.verification.repository;

import com.certificate.verification.model.Account;
import com.certificate.verification.model.AccountStatus;
import com.certificate.verification.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountCode(String accountCode);

    boolean existsByAccountCode(String accountCode);

    List<Account> findByStatus(AccountStatus status);

    List<Account> findByAccountType(AccountType accountType);

    List<Account> findByAccountNameContainingIgnoreCaseOrAccountCodeContainingIgnoreCase(
            String name, String code);
}
