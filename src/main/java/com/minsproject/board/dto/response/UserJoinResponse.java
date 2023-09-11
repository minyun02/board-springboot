package com.minsproject.board.dto.response;

import com.minsproject.board.dto.UserDto;

public record UserJoinResponse(
        Integer id,
        String username
) {
    public static UserJoinResponse from(UserDto user) {
        return new UserJoinResponse(
                user.getId(),
                user.getUsername()
        );
    }
}
