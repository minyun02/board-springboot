package com.minsproject.board.dto;

import com.minsproject.board.domain.entity.HashtagEntity;

import java.time.LocalDateTime;

public record HashtagDto(
        Integer id,
        String hashtagName,
        LocalDateTime registeredAt
) {
    public static HashtagDto of(String hashtagName) {
        return new HashtagDto(null, hashtagName, null);
    }

    public static HashtagDto of(Integer id, String hashtagName, LocalDateTime registeredAt) {
        return new HashtagDto(id, hashtagName, registeredAt);
    }

    public static HashtagDto from(HashtagEntity entity) {
        return new HashtagDto(
                entity.getId(),
                entity.getHashtagName(),
                entity.getRegisteredAt()
        );
    }
}
