package com.nikolahitek.emt.lab.service;

import com.nikolahitek.emt.lab.model.Account;
import com.nikolahitek.emt.lab.repository.AccountsRepository;
import com.nikolahitek.emt.lab.service.intefaces.IAccountsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AccountsService implements IAccountsService {

    private final AccountsRepository accountsRepository;
    private final ActivationAccountService activationAccountService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public AccountsService(AccountsRepository accountsRepository, ActivationAccountService activationAccountService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.accountsRepository = accountsRepository;
        this.activationAccountService = activationAccountService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUsernameValid(String username) {
        return username != null && !username.isEmpty() && !accountsRepository.findById(username).isPresent();
    }

    public boolean isPasswordValid(String password, String matchingPassword) {
        return password != null && !password.isEmpty() && password.equals(matchingPassword);
    }

    public void registerAccount(Account account) {
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        Account registered = accountsRepository.save(account);
        activationAccountService.generateActivationCodeForAccount(registered);
        emailService.sendActivationEmailToAccount(registered);
    }

    public boolean activateAccountByCode(String code) {
        Account account = activationAccountService.getAccountToActivate(code);
        if (account == null) return false;
        account.setActivated(true);
        accountsRepository.save(account);
        return true;
    }

    public Account getAccountByUsername(String username) {
        return accountsRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("Account not found."));
    }

    @Override
    public Account updateAccountFirstAndLastName(String username, String newFirstName, String newLastName) {
        return accountsRepository.findById(username)
                .map(user -> {
                    user.setFirstName(newFirstName);
                    user.setLastName(newLastName);
                    return accountsRepository.save(user);
                }).orElseThrow(() -> new RuntimeException("Account not found."));
    }

    @Override
    public Account updateAccountEmail(String username, String newEmail) {
        return accountsRepository.findById(username)
                .map(user -> {
                    user.setEmail(newEmail);
                    return accountsRepository.save(user);
                }).orElseThrow(() -> new RuntimeException("Account not found."));
    }

    public String changePassword(Account user, String currentPassword, String newPassword, String confNewPassword) {
        if (!isValidAccountPassword(user, currentPassword)) {
            return "Current password incorrect.";
        }
        if (!isPasswordValid(newPassword, confNewPassword)) {
            return "New passwords do not match.";
        }
        if (accountsRepository.findById(user.getUsername())
                .map(u -> {
                    u.setPassword(passwordEncoder.encode(newPassword));
                    return accountsRepository.save(u);
                }) != null) {
            return "Password successfully changed.";
        }
        return "Error occurred. Try again.";
    }

    private boolean isValidAccountPassword(Account account, String password) {
        return passwordEncoder.matches(password, account.getPassword());
    }

    public boolean resetPasswordForMail(String email) {
        Account user = accountsRepository.findByEmail(email)
                .orElse(null);
        if (user==null) return false;

        String newPassword = generateNewPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        emailService.sendPasswordResetMailForAccount(accountsRepository.save(user), newPassword);
        return true;
    }

    private String generateNewPassword() {
        String randomStr = UUID.randomUUID().toString().replace("-", "");
        return randomStr.substring(0,6);
    }

    public void deleteAccount(String username) {
        accountsRepository.deleteByUsername(username);
    }
}