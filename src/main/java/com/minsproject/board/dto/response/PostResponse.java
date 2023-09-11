package com.minsproject.board.dto.response;


import com.minsproject.board.dto.PostDto;

import java.time.LocalDateTime;

public record PostResponse(
        Integer id,
        String title,
        String body,
        UserResponse user,
        LocalDateTime registeredAt,
        LocalDateTime updatedAt
) {
    public static PostResponse fromPost(PostDto post) {
        return new PostResponse(
                post.id(),
                post.title(),
                post.body(),
                UserResponse.fromUser(post.user()),
                post.registeredAt(),
                post.updatedAt()
        );
    }
}
