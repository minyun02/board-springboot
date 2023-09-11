package com.minsproject.board.configuration.filter;

import com.minsproject.board.dto.UserDto;
import com.minsproject.board.service.UserService;
import com.minsproject.board.utils.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = "";

        try {
            if (header == null || !header.startsWith("Bearer=")) {
//                log.error("Authorization Header does not start with Bearer {}", request.getRequestURI());
//                filterChain.doFilter(request, response);
//                return;
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie c : cookies) {
                        String name = c.getName();

                        String value = c.getValue();
                        if (name.equals(HttpHeaders.AUTHORIZATION)) {
                            token = value.split("=")[1].trim();
                        }
                    }
                }
            } else {
                token = header.split("=")[1].trim();
            }

            String username = JwtTokenUtils.getUsername(token, secretKey);
            UserDto userDetails = userService.loadUserByUsername(username);

            if (!JwtTokenUtils.validate(token, userDetails.getUsername(), secretKey)) {
                filterChain.doFilter(request, response);
                return;
            }
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (RuntimeException e) {
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
