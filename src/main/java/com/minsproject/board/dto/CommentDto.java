package com.minsproject.board.dto;

import com.minsproject.board.domain.entity.CommentEntity;
import com.minsproject.board.domain.entity.PostEntity;
import com.minsproject.board.domain.entity.UserEntity;

import java.time.LocalDateTime;

public record CommentDto(
        Integer id,
        Integer postId,
        Integer userId,
        String username,
        String nickname,
        String content,
        Integer parentCommentId,
        LocalDateTime registeredAt,
        LocalDateTime updatedAt,
        LocalDateTime removedAt
) {
    public static CommentDto of(Integer postId, String username, String nickname, String content) {
        return CommentDto.of(postId, null, username, nickname, content, null);
    }

    public static CommentDto of(Integer postId, Integer userId, String username, String nickname, String content, Integer parentCommentId) {
        return CommentDto.of(null, postId, userId, username, nickname, content, parentCommentId, null, null, null);
    }

    public static CommentDto of(Integer id, Integer postId, Integer userId, String username, String nickname, String content, Integer parentCommentId, LocalDateTime registeredAt, LocalDateTime updatedAt, LocalDateTime removedAt) {
        return new CommentDto(id, postId, userId, username, nickname, content, parentCommentId, registeredAt, updatedAt, removedAt);
    }

    public static CommentDto fromEntity(CommentEntity entity) {
        String nickname = entity.getUser().getNickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = entity.getUser().getUsername();
        }
        return CommentDto.of(
                entity.getId(),
                entity.getPost().getId(),
                entity.getUser().getId(),
                entity.getUser().getUsername(),
                nickname,
                entity.getContent(),
                entity.getParentCommentId(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getRemovedAt()
        );
    }

    public CommentEntity toEntity(PostEntity post, UserEntity user) {
        return CommentEntity.of(post, user, content);
    }
}
