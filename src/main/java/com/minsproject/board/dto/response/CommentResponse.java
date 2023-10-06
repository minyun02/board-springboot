package com.minsproject.board.dto.response;

import com.minsproject.board.dto.CommentDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public record CommentResponse(
        Integer id,
        Integer postId,
        String username,
        String nickname,
        String content,
        Integer parentCommentId,
        LocalDateTime registeredAt,
        LocalDateTime updatedAt,
        LocalDateTime removedAt,
        Set<CommentResponse> childComments
) {
    public static CommentResponse of(
            Integer id,
            Integer postId,
            String username,
            String nickname,
            String content,
            LocalDateTime registeredAt
    ) {
        return CommentResponse.of(id, postId, username, nickname, content, null, registeredAt, null, null);
    }

    public static CommentResponse of(
            Integer id,
            Integer postId,
            String username,
            String nickname,
            String content,
            Integer parentCommentId,
            LocalDateTime registeredAt,
            LocalDateTime updatedAt,
            LocalDateTime removedAt
    ) {
        Comparator<CommentResponse> childCommentComparator = Comparator
                .comparing(CommentResponse::registeredAt)
                .thenComparingInt(CommentResponse::id);

        return new CommentResponse(
                id, postId, username, nickname, content, parentCommentId, registeredAt, updatedAt, removedAt, new TreeSet<>(childCommentComparator)
        );
    }
    public static CommentResponse from(CommentDto dto) {
        return CommentResponse.of(
                dto.id(),
                dto.postId(),
                dto.username(),
                dto.nickname(),
                dto.content(),
                dto.parentCommentId(),
                dto.registeredAt(),
                dto.updatedAt(),
                dto.removedAt()
        );
    }
    public boolean hasParentComment() {
        return parentCommentId != null;
    }
}
