package com.minsproject.board.dto.response;


import com.minsproject.board.dto.HashtagDto;
import com.minsproject.board.dto.PostDto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record PostResponse(
        Integer id,
        String title,
        String body,
        UserResponse user,
        Set<String> hashtags,
        LocalDateTime registeredAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(PostDto dto) {
        return new PostResponse(
                dto.id(),
                dto.title(),
                dto.body(),
                UserResponse.fromUser(dto.user()),
                dto.hashtagDtos().stream()
                                .map(HashtagDto::hashtagName)
                                .collect(Collectors.toUnmodifiableSet()),
                dto.registeredAt(),
                dto.updatedAt()
        );
    }
}
