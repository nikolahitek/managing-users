package com.nikolahitek.emt.lab.repository;

import com.nikolahitek.emt.lab.model.entity.ActivationCode;
import com.nikolahitek.emt.lab.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ActivationCodesRepository extends JpaRepository<ActivationCode, Long> {

    Optional<ActivationCode> findByUser(User user);
    Optional<ActivationCode> findByActivationCode(String code);
    List<ActivationCode> deleteByExpiryDateLessThan(Date now);
}
