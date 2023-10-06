package com.minsproject.board.dto.response;

import com.minsproject.board.domain.constant.AlarmType;
import com.minsproject.board.domain.entity.AlarmEntity;
import com.minsproject.board.dto.AlarmDto;
import com.minsproject.board.dto.UserDto;
import lombok.ToString;

import java.time.LocalDateTime;

public record AlarmResponse(
        Integer id,
        UserDto user,
        AlarmType alarmType,
        Integer fromUserId,
        Integer targetId,
        LocalDateTime registeredAt,
        LocalDateTime checkedAt
) {

    public static AlarmResponse from(AlarmDto dto) {
        return new AlarmResponse(
                dto.id(),
                dto.user(),
                dto.alarmType(),
                dto.fromUserId(),
                dto.targetId(),
                dto.registeredAt(),
                dto.checkedAt()
        );
    }
}
