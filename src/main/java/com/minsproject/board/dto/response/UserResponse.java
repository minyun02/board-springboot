package com.minsproject.board.dto.response;

import com.minsproject.board.dto.UserDto;

public record UserResponse(
        Integer id,
        String userName
) {
    public static UserResponse fromUser(UserDto user) {
        return new UserResponse(
                user.getId(),
                user.getUsername()
        );
    }

}
