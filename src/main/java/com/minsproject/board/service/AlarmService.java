package com.minsproject.board.service;

import com.minsproject.board.domain.entity.AlarmEntity;
import com.minsproject.board.exception.BoardException;
import com.minsproject.board.exception.ErrorCode;
import com.minsproject.board.repository.AlarmEntityRepository;
import com.minsproject.board.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class AlarmService {
    private final AlarmEntityRepository alarmEntityRepository;
    private final static Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private final static String ALARM_NAME = "alarm";
    private final EmitterRepository emitterRepository;

    public void send(Integer alarmId, Integer userId) {
        emitterRepository.get(userId).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(SseEmitter.event().id(alarmId.toString()).name(ALARM_NAME).data("new alarm"));
            } catch (IOException exception) {
                emitterRepository.delete(userId);
                throw new BoardException(ErrorCode.ALARM_CONNECT_ERROR);
            }
        }, () -> log.info("No emitter found"));
    }

    public SseEmitter connectAlarm(Integer userId) {
        SseEmitter sseEmitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitterRepository.save(userId, sseEmitter);
        sseEmitter.onCompletion(() -> emitterRepository.delete(userId));
        sseEmitter.onTimeout(() -> emitterRepository.delete(userId));

        try {
            sseEmitter.send(sseEmitter.event().id("").name("connect").data("connection completed"));
        } catch (IOException exception) {
            throw new BoardException(ErrorCode.ALARM_CONNECT_ERROR, String.format("Connecting alarm failed"));
        }

        return sseEmitter;
    }

    public Boolean checkAlarm(Integer alarmId) {
        AlarmEntity alarmEntity = alarmEntityRepository.findById(alarmId).orElseThrow(() -> new BoardException(ErrorCode.ALARM_NOT_FOUND));

        alarmEntity.setCheckedAt(LocalDateTime.now());

        AlarmEntity savedEntity = alarmEntityRepository.saveAndFlush(alarmEntity);
        return savedEntity.getId() == alarmId;
    }
}
