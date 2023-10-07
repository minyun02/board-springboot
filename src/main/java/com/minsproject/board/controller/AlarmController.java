package com.minsproject.board.controller;

import com.minsproject.board.dto.security.BoardPrincipal;
import com.minsproject.board.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@RequestMapping("/alarm")
@RestController
public class AlarmController {

    private final AlarmService alarmService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal BoardPrincipal boardPrincipal) {
        return alarmService.connectAlarm(boardPrincipal.id());
    }

}
