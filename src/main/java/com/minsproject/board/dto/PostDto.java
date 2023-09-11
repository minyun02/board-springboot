package com.minsproject.board.dto;

import com.minsproject.board.domain.entity.PostEntity;

import java.time.LocalDateTime;

public record PostDto(
        Integer id,

        String title,

        String body,

        UserDto user,

        LocalDateTime registeredAt,

        LocalDateTime updatedAt,

        LocalDateTime removedAt
) {
    public static PostDto of(String title, String body, UserDto user) {
        return new PostDto(
                null,
                title,
                body,
                user,
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
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }
}

