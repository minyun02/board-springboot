package com.minsproject.board.dto.security;

import com.minsproject.board.domain.constant.UserRole;
import com.minsproject.board.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public record BoardPrincipal(
        Integer id,
        String username,
        String password,
        String nickname,
        UserRole role,
        Map<String, Object> oAuth2Attributes,
        LocalDateTime removedAt

) implements UserDetails, OAuth2User {

    public static BoardPrincipal of(Integer id, String username, String password, String nickname, UserRole role, LocalDateTime removedAt) {
        return new BoardPrincipal(
                id, username, password, nickname, role, Map.of(), removedAt);
    }

    public static BoardPrincipal of(Integer id, String username, String password, String nickname, UserRole role, Map<String, Object> oAuth2Attributes, LocalDateTime removedAt) {
        return new BoardPrincipal(
                id, username, password, nickname, role, oAuth2Attributes, removedAt);
    }

    public static BoardPrincipal from(UserDto dto) {
        return BoardPrincipal.of(
                dto.getId(),
                dto.getUsername(),
                dto.getPassword(),
                dto.getNickname(),
                dto.getRole(),
                dto.getRemovedAt()
        );
    }

    public UserDto toDto() {
        return UserDto.of(
                id,
                username,
                password,
                nickname,
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

    @Override
    public Map<String, Object> getAttributes() { return oAuth2Attributes; }

    @Override
    public String getName() { return username; }
}
