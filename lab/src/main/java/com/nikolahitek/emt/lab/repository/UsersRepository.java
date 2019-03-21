package com.nikolahitek.emt.lab.repository;

import com.nikolahitek.emt.lab.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User, String> {
}
