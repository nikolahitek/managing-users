package com.nikolahitek.emt.lab.service;

import com.nikolahitek.emt.lab.model.entity.User;
import com.nikolahitek.emt.lab.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    private final static Logger logger = LoggerFactory.getLogger(ActivationAccountService.class);
    private final UsersRepository usersRepository;
    private final ActivationAccountService activationAccountService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public UsersService(UsersRepository usersRepository, ActivationAccountService activationAccountService, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.activationAccountService = activationAccountService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUsernameValid(String username) {
        return username != null && !username.isEmpty() && !usersRepository.findById(username).isPresent();
    }

    public boolean isPasswordValid(String password, String matchingPassword) {
        return password != null && !password.isEmpty() && password.equals(matchingPassword);
    }

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        logger.info("PASSWORD " + user.getPassword());
        User registered = usersRepository.save(user);
        activationAccountService.generateActivationCodeForUser(registered);
        emailService.sendActivationEmailToUser(registered);
    }

    public boolean activateUserByCode(String code) {
        User user = activationAccountService.getUserToActivate(code);
        if (user == null) {
            return false;
        }
        user.setActivated(true);
        usersRepository.save(user);
        return true;
    }

}