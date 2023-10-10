package com.minsproject.board.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호를 확인해주세요."),
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "중복된 Username입니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "User has invalid permission"),
    TOKEN_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Token Generation Failed"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Database error occurs"),
    ALARM_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Connect to notification occurs error"),
    ALARM_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "Alarm not found"),
    ;

    private final HttpStatus status;
    private final String message;
}
