package com.minsproject.board.dto.response;

public record LikeResponse(
        Integer totalLikes,
        Boolean hasLikedBefore
) {
    public static LikeResponse of(Integer totalLikes, Boolean hasLikedBefore) {
        return new LikeResponse(totalLikes, hasLikedBefore);
    }
}
