package com.nikolahitek.emt.lab.service;

import com.nikolahitek.emt.lab.model.Account;
import com.nikolahitek.emt.lab.model.ActivationCode;
import com.nikolahitek.emt.lab.repository.ActivationCodesRepository;
import com.nikolahitek.emt.lab.service.intefaces.IEmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService implements IEmailService {

    private final ActivationCodesRepository activationCodesRepository;
    private final JavaMailSender sender;


    public EmailService(ActivationCodesRepository activationCodesRepository, JavaMailSender sender) {
        this.activationCodesRepository = activationCodesRepository;
        this.sender = sender;
    }

    public void sendActivationEmailToAccount(Account account) {
        ActivationCode activationCode = activationCodesRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("Activation code not found."));
        try {
            sendActivationEmail(activationCode.getAccount(), activationCode.getActivationCode());
        } catch (Exception e) {
            throw new RuntimeException("Activation email failed to send.");
        }
    }

    private void sendActivationEmail(Account account, String activationCode) throws Exception{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(account.getEmail());
        helper.setSubject("Account Activation");
        helper.setText("<p>Dear " + account.getFirstName() + ", <p>" +
                "<p>Your activation code is <b>" + activationCode + "</b>.</p>" +
                "<p>To activate your account visit localhost:8080/activate or " +
                "<a href='http://localhost:8080/activate/" + activationCode + "'>click here</a>.</p>" +
                "<p>The activation code expires in 24 hours.</p>", true);

        sender.send(message);
    }

    public void sendPasswordResetMailForAccount(Account user, String password) {
        try {
            sendResetEmail(user, password);
        } catch (Exception e) {
            throw new RuntimeException("Reset email failed to send.");
        }
    }

    private void sendResetEmail(Account user, String password) throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(user.getEmail());
        helper.setSubject("Reset Password");
        helper.setText("<p>Dear " + user.getFirstName() + ", <p>" +
                "<p>Your new password is <b>" + password + "</b>.</p>" +
                "<p>You can now log in again.</p>", true);

        sender.send(message);
    }
}
