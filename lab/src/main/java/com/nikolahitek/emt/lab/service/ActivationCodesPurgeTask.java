package com.nikolahitek.emt.lab.service;

import com.nikolahitek.emt.lab.model.entity.User;
import com.nikolahitek.emt.lab.repository.ActivationCodesRepository;
import com.nikolahitek.emt.lab.repository.UsersRepository;
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
    private final UsersRepository usersRepository;
    private final static Logger logger = LoggerFactory.getLogger(ActivationCodesPurgeTask.class);

    public ActivationCodesPurgeTask(ActivationCodesRepository activationCodesRepository, UsersRepository usersRepository) {
        this.activationCodesRepository = activationCodesRepository;
        this.usersRepository = usersRepository;
    }

    @Scheduled(cron = "0 0 1 * * *", zone = "Europe/Budapest")
    public void purgeExpired() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        Date now = new Date(cal.getTime().getTime());

        activationCodesRepository.deleteByExpiryDateLessThan(now)
                .forEach(activationCode -> {
                    User user = activationCode.getUser();
                    if (!user.getActivated()) {
                        logger.info("REMOVED EXPIRED REGISTRATION FOR USER: " + activationCode.getUser().getUsername());
                        usersRepository.deleteByUsername(activationCode.getUser().getUsername());
                    }
                });
    }
}
