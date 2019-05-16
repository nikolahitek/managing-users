package com.nikolahitek.emt.lab.repository;

import com.nikolahitek.emt.lab.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Account, String> {

    void deleteByUsername(String username);

    Optional<Account> findByEmail(String email);
}
