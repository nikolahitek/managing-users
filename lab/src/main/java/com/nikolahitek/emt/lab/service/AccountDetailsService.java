package com.nikolahitek.emt.lab.service;


import com.nikolahitek.emt.lab.repository.AccountsRepository;
import com.nikolahitek.emt.lab.security.AccountDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final AccountsRepository accountsRepository;

    @Autowired
    public AccountDetailsService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountsRepository.findById(username)
                .map(AccountDetails::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Account with username [" + username + "] does not exist."));
    }
}
