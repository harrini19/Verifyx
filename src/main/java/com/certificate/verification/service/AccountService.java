package com.certificate.verification.service;

import com.certificate.verification.dto.AccountRequest;
import com.certificate.verification.exception.DuplicateResourceException;
import com.certificate.verification.exception.ResourceNotFoundException;
import com.certificate.verification.model.Account;
import com.certificate.verification.model.AccountStatus;
import com.certificate.verification.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for Chart of Accounts (CoA) management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public Account createAccount(AccountRequest request) {
        if (accountRepository.existsByAccountCode(request.getAccountCode())) {
            throw new DuplicateResourceException(
                    "Account code already exists: " + request.getAccountCode());
        }

        Account account = Account.builder()
                .accountCode(request.getAccountCode())
                .accountName(request.getAccountName())
                .accountType(request.getAccountType())
                .description(request.getDescription())
                .openingBalance(request.getOpeningBalance())
                .currentBalance(request.getOpeningBalance())
                .status(AccountStatus.ACTIVE)
                .build();

        Account saved = accountRepository.save(account);
        log.info("Account created: {} - {}", saved.getAccountCode(), saved.getAccountName());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account", "id", id));
    }

    @Transactional
    public Account updateAccount(Long id, AccountRequest request) {
        Account account = getAccountById(id);

        // If code changed, check uniqueness
        if (!account.getAccountCode().equals(request.getAccountCode()) &&
                accountRepository.existsByAccountCode(request.getAccountCode())) {
            throw new DuplicateResourceException(
                    "Account code already exists: " + request.getAccountCode());
        }

        account.setAccountCode(request.getAccountCode());
        account.setAccountName(request.getAccountName());
        account.setAccountType(request.getAccountType());
        account.setDescription(request.getDescription());

        Account saved = accountRepository.save(account);
        log.info("Account updated: {} - {}", saved.getAccountCode(), saved.getAccountName());
        return saved;
    }

    @Transactional
    public Account toggleStatus(Long id) {
        Account account = getAccountById(id);
        account.setStatus(account.getStatus() == AccountStatus.ACTIVE
                ? AccountStatus.INACTIVE
                : AccountStatus.ACTIVE);
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<Account> searchAccounts(String query) {
        return accountRepository
                .findByAccountNameContainingIgnoreCaseOrAccountCodeContainingIgnoreCase(query, query);
    }
}
