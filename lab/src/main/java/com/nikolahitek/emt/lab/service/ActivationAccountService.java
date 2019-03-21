package com.nikolahitek.emt.lab.service;

import com.nikolahitek.emt.lab.model.entity.ActivationCode;
import com.nikolahitek.emt.lab.model.entity.User;
import com.nikolahitek.emt.lab.repository.ActivationCodesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ActivationAccountService {

    private final static Logger logger = LoggerFactory.getLogger(ActivationAccountService.class);
    private final ActivationCodesRepository activationCodesRepository;

    public ActivationAccountService(ActivationCodesRepository activationCodesRepository) {
        this.activationCodesRepository = activationCodesRepository;
    }

    public void generateActivationCodeForUser(User user) {
        String code = UUID.randomUUID().toString().replace("-", "");
        ActivationCode activationCode = new ActivationCode(code, user);
        activationCode.calculateExpiryDate();
        activationCodesRepository.save(activationCode);
    }

    public User getUserToActivate(String code) {
        logger.info("AAS Code: " + code);
        return activationCodesRepository.findByActivationCode(code)
                .map(ActivationCode::getUser).orElse(null);
    }
}
