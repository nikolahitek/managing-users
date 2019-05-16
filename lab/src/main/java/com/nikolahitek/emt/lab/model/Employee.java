package com.nikolahitek.emt.lab.model;

import com.nikolahitek.emt.lab.consts.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class Employee extends Account {
    @ManyToOne
    @JoinColumn(name="dep_id")
    private Department department;

    public Employee(String username, String firstName, String lastName, String email,
                    String password, Boolean activated, Department department) {
        super(username, firstName, lastName, email, password, activated, Role.EMPLOYEE);
        this.department = department;
    }


}
