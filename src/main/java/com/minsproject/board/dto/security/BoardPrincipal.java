package com.minsproject.board.dto.security;

import com.minsproject.board.domain.constant.UserRole;
import com.minsproject.board.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public record BoardPrincipal(
        Integer id,
        String username,
        String password,
        UserRole role,
        LocalDateTime removedAt

) implements UserDetails {

    public static BoardPrincipal of(Integer id, String username, String password, UserRole role, LocalDateTime removedAt) {
        return new BoardPrincipal(
                id, username, password, role, removedAt);
    }

    public static BoardPrincipal from(UserDto dto) {
        return BoardPrincipal.of(
                dto.getId(),
                dto.getUsername(),
                dto.getPassword(),
                dto.getRole(),
                dto.getRemovedAt()
        );
    }

    public UserDto toDto() {
        return UserDto.of(
                id,
                username,
                password,
                role
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.toString()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return removedAt == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return removedAt == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return removedAt == null;
    }

    @Override
    public boolean isEnabled() {
        return removedAt == null;
    }
}
