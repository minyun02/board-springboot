package com.minsproject.board.producer;

import com.minsproject.board.dto.event.AlarmEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AlarmProducer {

    private static final String TOPIC = "alarm";

    private final KafkaTemplate<Integer, AlarmEvent> kafkaTemplate;

    public void send(AlarmEvent event) {
        kafkaTemplate.send(TOPIC, event.receiverUserId(), event);
        log.info("Send to Kafka finished");
    }
}
