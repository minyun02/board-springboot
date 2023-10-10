package com.minsproject.board.service;

import com.minsproject.board.domain.entity.AlarmEntity;
import com.minsproject.board.dto.AlarmDto;
import com.minsproject.board.dto.request.UserAuthRequest;
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

    public boolean checkDuplicatedUsername(String username) {
        return userEntityRepository.findByUsername(username).isPresent();
    }

    @Transactional
    public UserDto join(UserAuthRequest user) {
        UserEntity savedUser = userEntityRepository.save(UserEntity.of(user.username(), encoder.encode(user.password()), user.nickname()));
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
