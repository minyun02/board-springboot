package com.minsproject.board.dto;

import com.minsproject.board.domain.entity.PostEntity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record PostDto(
        Integer id,

        String title,

        String body,

        UserDto user,

        Set<HashtagDto> hashtagDtos,

        LocalDateTime registeredAt,

        LocalDateTime updatedAt,

        LocalDateTime removedAt
) {
    public static PostDto of(String title, String body, UserDto user, Set<HashtagDto> hashtagDtos) {
        return new PostDto(
                null,
                title,
                body,
                user,
                hashtagDtos,
                null,
                null,
                null
        );
    }

    public static PostDto fromEntity(PostEntity entity) {
        return new PostDto(
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                UserDto.fromEntity(entity.getUser()),
                entity.getHashtags().stream()
                                .map(HashtagDto::from)
                                .collect(Collectors.toUnmodifiableSet()),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }
}

