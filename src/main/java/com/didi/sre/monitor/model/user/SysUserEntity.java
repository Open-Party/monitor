package com.didi.sre.monitor.model.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author soarpenguin on 17-8-24.
 */
public class SysUserEntity implements UserDetails, java.io.Serializable {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Date dob;
    private boolean deleted;

    private List<SysRoleEntity> roles;

    public SysUserEntity() {
    }

    public SysUserEntity(String username, String email, String password, Date dob, boolean deleted, List<SysRoleEntity> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.dob = dob;
        this.deleted = deleted;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SysRoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRoleEntity> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
//      List<SysRoleEntity> roles = this.getRoles();
//      for (SysRoleEntity role : roles) {
//          auths.add(new SimpleGrantedAuthority(role.getName()));
//      }
        auths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return auths;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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
        return true;
    }
}
