package com.nikolahitek.emt.lab.service.intefaces;

import com.nikolahitek.emt.lab.model.Account;
import org.springframework.stereotype.Service;

@Service
public interface IEmailService {

    void sendActivationEmailToAccount(Account account);

    void sendPasswordResetMailForAccount(Account account, String password);
}
