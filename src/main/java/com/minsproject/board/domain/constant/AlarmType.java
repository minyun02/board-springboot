package com.minsproject.board.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlarmType {

    NEW_COMMENT("회원님 글에 새로운 댓글이 달렸습니다!"),
    NEW_LIKE("누군가가 회원님 글을 좋아했어요!"),
    ;

    private final String alarmText;
}
