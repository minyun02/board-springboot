package com.minsproject.board.dto.request;

public record UserJoinRequest(
        String username,
        String password,
        String nickname
) {

}
