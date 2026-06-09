package com.nguyenhien.auth_service.application.event.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.nguyenhien.auth_service.application.event.message.UserRegisterEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishUserRegistered(UserRegisterEvent event) {
        kafkaTemplate.send("user.registered", event);
    }
}
