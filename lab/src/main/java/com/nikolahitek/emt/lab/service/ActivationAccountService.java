package com.nikolahitek.emt.lab.service;

import com.nikolahitek.emt.lab.model.ActivationCode;
import com.nikolahitek.emt.lab.model.Account;
import com.nikolahitek.emt.lab.repository.ActivationCodesRepository;
import com.nikolahitek.emt.lab.service.intefaces.IActivationAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
public class ActivationAccountService implements IActivationAccountService {

    private final static Logger logger = LoggerFactory.getLogger(ActivationAccountService.class);
    private final ActivationCodesRepository activationCodesRepository;

    public ActivationAccountService(ActivationCodesRepository activationCodesRepository) {
        this.activationCodesRepository = activationCodesRepository;
    }

    public void generateActivationCodeForAccount(Account user) {
        String code = UUID.randomUUID().toString().replace("-", "");
        ActivationCode activationCode = new ActivationCode(code, user);
        activationCode.calculateExpiryDate();
        activationCodesRepository.save(activationCode);
    }

    public Account getAccountToActivate(String code) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        Date now = new Date(cal.getTime().getTime());

        logger.info("NOW: " + now.toString());

        return activationCodesRepository.findByActivationCode(code)
                .map(activationCode -> {
                    Date expiration = new Date(activationCode.getExpiryDate().getTime());
                    logger.info("EXPIRATION: " + expiration.toString());
                    if (!expiration.before(now)) {
                        return activationCode.getAccount();
                    }
                    return null;
                }).orElse(null);
    }
}
