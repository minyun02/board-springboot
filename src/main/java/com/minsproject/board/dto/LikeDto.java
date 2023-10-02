package com.minsproject.board.dto;

import com.minsproject.board.domain.entity.LikeEntity;

import java.time.LocalDateTime;

public record LikeDto(
        Integer id,
        Integer userId,
        String userName,
        Integer postId,
        LocalDateTime registeredAt,
        LocalDateTime updatedAt,
        LocalDateTime removedAt
) {
    public static LikeDto from(LikeEntity entity) {
        return new LikeDto(
                entity.getId(),
                entity.getUser().getId(),
                entity.getUser().getUsername(),
                entity.getPost().getId(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }
}
