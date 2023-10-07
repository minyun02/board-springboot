package com.minsproject.board.controller;

import com.minsproject.board.dto.security.BoardPrincipal;
import com.minsproject.board.service.AlarmService;
import com.minsproject.board.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/alarm")
@RestController
public class AlarmController {

    private final AlarmService alarmService;
    private final UserService userService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        return alarmService.connectAlarm(boardPrincipal.id());
    }

    @GetMapping("/unchecked")
    public Integer getUnchecked(@AuthenticationPrincipal BoardPrincipal boardPrincipal, Pageable pageable) {
        log.info("getAlarm from /unchecked");
        return userService.getAlarm(boardPrincipal.id(), pageable).stream()
                .filter(alarm -> alarm.checkedAt() == null)
                .toList()
                .size();
    }

    @GetMapping("/check/{alarmId}")
    public Boolean checkAlarm(@PathVariable Integer alarmId) {
        return alarmService.checkAlarm(alarmId);
    }

}
