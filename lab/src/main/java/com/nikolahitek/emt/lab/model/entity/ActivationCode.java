package com.nikolahitek.emt.lab.model.entity;

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
    private User user;
    private Date expiryDate;

    public ActivationCode(String activationCode, User user) {
        this.activationCode = activationCode;
        this.user = user;
    }

    public void calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        this.expiryDate = new Date(cal.getTime().getTime());
    }
}
