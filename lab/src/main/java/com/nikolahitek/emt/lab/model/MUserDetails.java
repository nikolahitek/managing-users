package com.nikolahitek.emt.lab.model;

import com.nikolahitek.emt.lab.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MUserDetails extends User implements UserDetails {

    public MUserDetails(final User user){
        super(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getActivated());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        //INSTEAD OF "USER" ADD THE USERS ROLE FROM NEW ROLES RELATION
        roles.add(new SimpleGrantedAuthority("ROLE_" + "USER"));
        return roles;
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return super.getActivated();
    }
}
