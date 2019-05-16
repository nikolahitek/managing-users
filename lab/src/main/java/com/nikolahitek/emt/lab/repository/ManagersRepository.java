package com.nikolahitek.emt.lab.repository;

import com.nikolahitek.emt.lab.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagersRepository extends JpaRepository<Manager, String> {

    void deleteByUsername(String username);
}
