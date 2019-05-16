package com.nikolahitek.emt.lab.repository;

import com.nikolahitek.emt.lab.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentsRepository extends JpaRepository<Department, Long> {
    Optional<Department> findFirstByName(String name);
}
