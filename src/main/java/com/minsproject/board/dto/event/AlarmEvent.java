package com.minsproject.board.dto.event;

import com.minsproject.board.domain.constant.AlarmType;

public record AlarmEvent(
        Integer receiverUserId,
        AlarmType alarmType,
        Integer fromUserId,
        Integer targetId
) {
}
