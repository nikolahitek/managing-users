package com.nikolahitek.emt.lab.service;

import com.nikolahitek.emt.lab.model.entity.ActivationCode;
import com.nikolahitek.emt.lab.model.entity.User;
import com.nikolahitek.emt.lab.repository.ActivationCodesRepository;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final ActivationCodesRepository activationCodesRepository;
    private final JavaMailSender sender;


    public EmailService(ActivationCodesRepository activationCodesRepository, JavaMailSender sender) {
        this.activationCodesRepository = activationCodesRepository;
        this.sender = sender;
    }

    public void sendActivationEmailToUser(User user) {
        ActivationCode activationCode = activationCodesRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Activation code not found."));
        try {
            sendEmail(activationCode.getUser(), activationCode.getActivationCode());
        } catch (Exception e) {
            throw new RuntimeException("Activation email failed to send.");
        }
    }

    private void sendEmail(User user, String activationCode) throws Exception{
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(user.getEmail());
        helper.setSubject("Account Activation");
        helper.setText("<p>Dear " + user.getFirstName() + ", <p>" +
                "<p>Your activation code is <b>" + activationCode + "</b>.</p>" +
                "<p>To activate your account visit localhost:8080/activate or " +
                "<a href='http://localhost:8080/activate/" + activationCode + "'>click here</a>.</p>" +
                "<p>The activation code expires in 24 hours.</p>", true);

        sender.send(message);
    }
}
