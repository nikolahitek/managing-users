package com.nikolahitek.emt.lab.model;

import com.nikolahitek.emt.lab.consts.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class Manager extends Account {
    @OneToOne
    @JoinColumn(name="dep_id")
    private Department department;

    public Manager(String username, String firstName, String lastName, String email,
                    String password, Boolean activated, Department department) {
        super(username, firstName, lastName, email, password, activated, Role.MANAGER);
        this.department = department;
    }
}
