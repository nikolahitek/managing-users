package com.nikolahitek.emt.lab.service.intefaces;

import com.nikolahitek.emt.lab.model.Account;
import org.springframework.stereotype.Service;

@Service
public interface IAccountsService {

    boolean isUsernameValid(String username);

    boolean isPasswordValid(String password, String matchingPassword);

    void registerAccount(Account account);

    boolean activateAccountByCode(String code);

    Account getAccountByUsername(String username);

    Account updateAccountFirstAndLastName(String username, String newFirstName, String newLastName);

    Account updateAccountEmail(String username, String newEmail);

    void deleteAccount(String username);

    String changePassword(Account account, String currentPassword, String newPassword, String confNewPassword);

    boolean resetPasswordForMail(String email);

}