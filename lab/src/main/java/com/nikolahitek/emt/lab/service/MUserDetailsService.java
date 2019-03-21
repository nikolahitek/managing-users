package com.nikolahitek.emt.lab.service;


import com.nikolahitek.emt.lab.model.MUserDetails;
import com.nikolahitek.emt.lab.model.entity.User;
import com.nikolahitek.emt.lab.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UsersRepository usersRepository;

    @Autowired
    public MUserDetailsService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usersRepository.findById(username)
                .map(MUserDetails::new)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with username [" + username + "] does not exist."));
    }

    public Optional<User> getCurrentUser(String username) {
        return usersRepository.findById(username);
    }
}
