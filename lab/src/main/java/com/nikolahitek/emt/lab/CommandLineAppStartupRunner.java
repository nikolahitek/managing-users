package com.nikolahitek.emt.lab;

import com.nikolahitek.emt.lab.consts.Role;
import com.nikolahitek.emt.lab.model.Account;
import com.nikolahitek.emt.lab.repository.AccountsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    @Value("${admin.username}")
    private String username;
    @Value("${admin.firstname}")
    private String firstName;
    @Value("${admin.lastname}")
    private String lastName;
    @Value("${admin.email}")
    private String email;
    @Value("${admin.password}")
    private String password;

    private final AccountsRepository accountsRepository;
    private final PasswordEncoder passwordEncoder;

    public CommandLineAppStartupRunner(AccountsRepository accountsRepository, PasswordEncoder passwordEncoder) {
        this.accountsRepository = accountsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String...args) {
        password = passwordEncoder.encode(password);
        Account admin = new Account(username, firstName, lastName, email, password, true, Role.ADMIN);
        accountsRepository.save(admin);
    }
}
