package com.minsproject.board.dto;

import com.minsproject.board.domain.entity.PostEntity;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record PostWithCommentsDto(
        Integer id,
        String title,
        String body,
        UserDto user,
        LocalDateTime registeredAt,
        LocalDateTime updatedAt,
        LocalDateTime removedAt,
        Set<CommentDto> commentsDtos,
        Set<HashtagDto> hashtagDtos

) {
    public static PostWithCommentsDto of(
            Integer id,
            String title,
            String body,
            UserDto user,
            LocalDateTime registeredAt,
            LocalDateTime updatedAt,
            LocalDateTime removedAt,
            Set<CommentDto> commentsDtos,
            Set<HashtagDto> hashtagDtos
    ) {
        return new PostWithCommentsDto(
                id, title, body, user, registeredAt, updatedAt, removedAt, commentsDtos, hashtagDtos);
    }
    public static PostWithCommentsDto from(PostEntity entity) {
        return new PostWithCommentsDto(
                entity.getId(),
                entity.getTitle(),
                entity.getBody(),
                UserDto.fromEntity(entity.getUser()),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt(),
                entity.getComments().stream()
                        .map(CommentDto::fromEntity)
                        .collect(Collectors.toCollection(LinkedHashSet::new)),
                entity.getHashtags().stream()
                        .map(HashtagDto::from)
                        .collect(Collectors.toUnmodifiableSet())
        );
    }
}
