package com.minsproject.board.dto;

import com.minsproject.board.domain.constant.AlarmType;
import com.minsproject.board.domain.entity.AlarmEntity;

import java.time.LocalDateTime;

public record AlarmDto(
        Integer id,
        UserDto user,
        AlarmType alarmType,
        Integer fromUserId,
        Integer targetId,
        LocalDateTime registeredAt,
        LocalDateTime checkedAt
) {
    public static AlarmDto fromEntity(AlarmEntity entity) {
        return new AlarmDto(
                entity.getId(),
                UserDto.fromEntity(entity.getUser()),
                entity.getAlarmType(),
                entity.getFromUserId(),
                entity.getTargetId(),
                entity.getRegisteredAt(),
                entity.getCheckedAt()
        );
    }
}
