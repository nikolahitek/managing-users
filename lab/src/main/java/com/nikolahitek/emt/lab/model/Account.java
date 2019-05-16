package com.nikolahitek.emt.lab.model;

import com.nikolahitek.emt.lab.consts.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data public class Account {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "firstname")
    private String firstName;
    @Column(name = "lastname")
    private String lastName;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "activated")
    private Boolean activated;
    @Column(name = "role")
    private Role role;

    public Department getDepartment() {
        return null;
    }
    public void setDepartment(Department department) { }
}
