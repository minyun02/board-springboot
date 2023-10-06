package com.minsproject.board.dto.request;

import com.minsproject.board.dto.CommentDto;
import com.minsproject.board.dto.UserDto;

public record CommentRequest(
        Integer postId,
        Integer parentCommentId,
        String content
) {
    public static CommentRequest of(Integer postId, String content) {
        return CommentRequest.of(postId, null, content);
    }

    public static CommentRequest of(Integer postId, Integer parentCommentId, String content) {
        return CommentRequest.of(postId, parentCommentId, content);
    }

    public CommentDto toDto(UserDto user) {
        return CommentDto.of(
                postId,
                user.getId(),
                user.getUsername(),
                user.getNickname(),
                content,
                parentCommentId
        );
    }
}
