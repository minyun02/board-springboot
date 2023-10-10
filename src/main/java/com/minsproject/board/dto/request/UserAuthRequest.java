package com.minsproject.board.dto.request;

public record UserAuthRequest(
        String username,
        String password,
        String nickname
) { }
