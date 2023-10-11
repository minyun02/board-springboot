package com.minsproject.board.consumer;

import com.minsproject.board.dto.event.AlarmEvent;
import com.minsproject.board.producer.AlarmProducer;
import com.minsproject.board.service.AlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AlarmConsumer {

    private final AlarmService alarmService;

    @KafkaListener(topics = "alarm")
    public void consumeAlarm(AlarmEvent event, Acknowledgment ack) {
        log.info("Consume the event {}", event);
        alarmService.send(event.alarmType(), event.fromUserId(), event.targetId(), event.receiverUserId());
        ack.acknowledge();
    }
}
