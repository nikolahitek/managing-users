package com.nikolahitek.emt.lab.repository;

import com.nikolahitek.emt.lab.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeesRepository extends JpaRepository<Employee, String> {

    void deleteByUsername(String username);
}
