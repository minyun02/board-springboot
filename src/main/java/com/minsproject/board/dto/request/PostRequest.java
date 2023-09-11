package com.minsproject.board.dto.request;

import com.minsproject.board.dto.UserDto;
import com.minsproject.board.dto.PostDto;

public record PostRequest(
        String title,

        String body
) {
//    public static PostRequest of(String title, String body) {
//        return new PostRequest(title, body);
//    }

    public PostDto toDto(UserDto user) {
        return PostDto.of(
                title,
                body,
                user
        );
    }

}
