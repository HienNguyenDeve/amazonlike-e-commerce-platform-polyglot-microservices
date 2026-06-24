package com.nguyenhien.user_service.application.events.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nguyenhien.user_service.application.events.message.UserRegisteredEvent;
import com.nguyenhien.user_service.application.services.UserProfileService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRegisteredConsumer {
    private final UserProfileService userProfileService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user.registered", groupId = "user-service")
    public void consume(String message) {
        try {
            System.out.println("RECEIVED EVENT");
            UserRegisteredEvent event = objectMapper.readValue(message, UserRegisteredEvent.class);
            userProfileService.createProfile(event.authUserId(), event.email());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
