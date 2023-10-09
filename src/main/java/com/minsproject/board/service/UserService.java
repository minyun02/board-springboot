package com.minsproject.board.service;

import com.minsproject.board.domain.entity.AlarmEntity;
import com.minsproject.board.dto.AlarmDto;
import com.minsproject.board.repository.AlarmEntityRepository;
import com.minsproject.board.domain.constant.ErrorCode;
import com.minsproject.board.exception.BoardException;
import com.minsproject.board.dto.UserDto;
import com.minsproject.board.domain.entity.UserEntity;
import com.minsproject.board.repository.UserEntityRepository;
import com.minsproject.board.utils.JwtTokenUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;

    private final AlarmEntityRepository alarmEntityRepository;

    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public UserDto loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository.findByUsername(username).map(UserDto::fromEntity).orElseThrow(
                    () -> new BoardException(ErrorCode.USER_NOT_FOUND, String.format("username = %s", username)
                ));
    }


    @Transactional
    public UserDto join(String username, String password, @RequestParam(required = false) String nickname) {
        userEntityRepository.findByUsername(username).ifPresent(it -> {

            throw new BoardException(ErrorCode.DUPLICATED_USER_NAME, String.format("이미 존재하는 username = %s", username));
        });

        UserEntity savedUser = userEntityRepository.save(UserEntity.of(username, encoder.encode(password), nickname));
        return UserDto.fromEntity(savedUser);
    }

    public void login(String username, String password, HttpServletResponse response) {
        UserDto savedUser = loadUserByUsername(username);
        if (!encoder.matches(password, savedUser.getPassword())) {
            throw new BoardException(ErrorCode.INVALID_PASSWORD);
        }

        String token = JwtTokenUtils.generateAccessToken(username, secretKey, expiredTimeMs);
        if (token == null || token.isEmpty()) {
            throw new BoardException(ErrorCode.TOKEN_GENERATION_FAILED);
        }

        //토큰을 쿠키로 발급 및 응답에 추가
        Cookie cookie = new Cookie(HttpHeaders.AUTHORIZATION, token);
//        cookie.setAttribute("username", savedUser.getUsername());
        cookie.setMaxAge(7 * 24 * 60 * 60);//7일
        cookie.setPath("/");
        cookie.setDomain("localhost");
        cookie.setSecure(false);

        response.addCookie(cookie);
    }

    @Transactional
    public Optional<UserDto> searchUser(String username) {
        return userEntityRepository.findByUsername(username)
                .map(UserDto::fromEntity);
    }

    public UserDto saveUser(String username, String password, String nickname) {
        return UserDto.fromEntity(userEntityRepository.save(UserEntity.of(username, password, nickname)));
    }

    public Page<AlarmDto> getAlarm(Integer userId, Pageable pageable) {
        Page<AlarmEntity> alarm = alarmEntityRepository.findAllByUserId(userId, pageable);

        return alarm.map(AlarmDto::fromEntity);
    }
}
