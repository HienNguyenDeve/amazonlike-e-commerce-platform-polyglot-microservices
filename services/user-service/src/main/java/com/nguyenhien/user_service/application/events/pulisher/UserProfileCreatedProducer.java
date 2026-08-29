package com.nguyenhien.user_service.application.events.pulisher;

import com.nguyenhien.user_service.application.events.message.UserProfileCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserProfileCreatedProducer {
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public void publishUserProfileCreated(UserProfileCreatedEvent event) {
    kafkaTemplate.send("user.profile.created", event);
  }
}
