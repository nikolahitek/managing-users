package com.nikolahitek.emt.lab.service.intefaces;

import com.nikolahitek.emt.lab.model.Account;
import org.springframework.stereotype.Service;

@Service
public interface IActivationAccountService {

    void generateActivationCodeForAccount(Account account);

    Account getAccountToActivate(String code);

}
