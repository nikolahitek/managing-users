package com.nikolahitek.emt.lab.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Table
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data public class ActivationCode {

    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String activationCode;
    @OneToOne
    @JoinColumn
    private Account account;
    private Date expiryDate;

    public ActivationCode(String activationCode, Account account) {
        this.activationCode = activationCode;
        this.account = account;
    }

    public void calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        this.expiryDate = new Date(cal.getTime().getTime());
    }
}
