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
        String username,
        String nickname,
        Set<String> hashtags,
        LocalDateTime registeredAt,
        LocalDateTime updatedAt
) {
    public static PostResponse from(PostDto dto) {
        String nickname = dto.user().getNickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.user().getUsername();
        }
        return new PostResponse(
                dto.id(),
                dto.title(),
                dto.body(),
                dto.user().getUsername(),
                nickname,
                dto.hashtagDtos().stream()
                                .map(HashtagDto::hashtagName)
                                .collect(Collectors.toUnmodifiableSet()),
                dto.registeredAt(),
                dto.updatedAt()
        );
    }
}
