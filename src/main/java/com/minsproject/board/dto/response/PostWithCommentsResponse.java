package com.minsproject.board.dto.response;

import com.minsproject.board.dto.CommentDto;
import com.minsproject.board.dto.HashtagDto;
import com.minsproject.board.dto.PostWithCommentsDto;
import com.minsproject.board.dto.UserDto;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

public record PostWithCommentsResponse(
        Integer id,
        String title,
        String body,
        UserDto user,
        LocalDateTime registeredAt,
        LocalDateTime updatedAt,
        LocalDateTime removedAt,
        Set<CommentResponse> commentResponses,
        Set<String> hashtags
) {
    public static PostWithCommentsResponse of(
            Integer id,
            String title,
            String body,
            UserDto user,
            LocalDateTime registeredAt,
            LocalDateTime updatedAt,
            LocalDateTime removedAt,
            Set<CommentResponse> commentResponses,
            Set<String> hashtags
    ) {
        return new PostWithCommentsResponse(
                id, title, body, user, registeredAt, updatedAt, removedAt, commentResponses, hashtags);
    }
    public static PostWithCommentsResponse from(PostWithCommentsDto dto) {
        return new PostWithCommentsResponse(
                dto.id(),
                dto.title(),
                dto.body(),
                dto.user(),
                dto.registeredAt(),
                dto.updatedAt(),
                dto.removedAt(),
                organizeChildComments(dto.commentsDtos()),
                dto.hashtagDtos().stream()
                        .map(HashtagDto::hashtagName)
                        .collect(Collectors.toUnmodifiableSet())
        );
    }

    private static Set<CommentResponse> organizeChildComments(Set<CommentDto> dtos) {
        Map<Integer, CommentResponse> map = dtos.stream()
                .map(CommentResponse::from)
                .collect(Collectors.toMap(CommentResponse::id, Function.identity()));

        map.values().stream()
                .filter(CommentResponse::hasParentComment)
                .forEach(comment -> {
                    CommentResponse parentComment = map.get(comment.parentCommentId());
                    parentComment.childComments().add(comment);
                });

        return map.values().stream()
                .filter(comment -> !comment.hasParentComment())
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator
                                .comparing(CommentResponse::registeredAt)
                                .reversed()
                                .thenComparingInt(CommentResponse::id)
                        )
                ));
    }
}
