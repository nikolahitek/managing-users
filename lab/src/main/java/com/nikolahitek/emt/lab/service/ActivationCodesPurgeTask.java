package com.nikolahitek.emt.lab.service;

import com.nikolahitek.emt.lab.model.Account;
import com.nikolahitek.emt.lab.repository.ActivationCodesRepository;
import com.nikolahitek.emt.lab.repository.AccountsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Service
@Transactional
public class ActivationCodesPurgeTask {

    private final ActivationCodesRepository activationCodesRepository;
    private final AccountsRepository accountsRepository;
    private final static Logger logger = LoggerFactory.getLogger(ActivationCodesPurgeTask.class);

    public ActivationCodesPurgeTask(ActivationCodesRepository activationCodesRepository, AccountsRepository accountsRepository) {
        this.activationCodesRepository = activationCodesRepository;
        this.accountsRepository = accountsRepository;
    }

    @Scheduled(cron = "0 0 1 * * *", zone = "Europe/Budapest")
    public void purgeExpired() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        Date now = new Date(cal.getTime().getTime());

        activationCodesRepository.deleteByExpiryDateLessThan(now)
                .forEach(activationCode -> {
                    Account account = activationCode.getAccount();
                    if (!account.getActivated()) {
                        logger.info("REMOVED EXPIRED REGISTRATION FOR USER: " + activationCode.getAccount().getUsername());
                        accountsRepository.deleteByUsername(activationCode.getAccount().getUsername());
                    }
                });
    }
}
