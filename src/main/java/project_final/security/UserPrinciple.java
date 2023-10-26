package project_final.security;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project_final.entity.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import java.util.*;

import java.util.stream.Collectors;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrinciple implements UserDetails {
    private Long id;
    private String name;
    private String email;
    private String avatar;
    private String username;
    @JsonIgnore
    private String password;
    private boolean status;
    private boolean active;
    private List<GrantedAuthority> authorities;

    public static UserPrinciple build(User user){
        List<GrantedAuthority> authorities = user.getRoles().stream().map(
                role -> new SimpleGrantedAuthority(role.getRoleName().name())
        ).collect(Collectors.toList());
        return UserPrinciple.builder()
                .id(user.getId())
                .name(user.getName())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .status(user.isStatus())
                .active(user.isActive())
                .authorities(authorities).build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
     return    this.status && this.active;
    }


}