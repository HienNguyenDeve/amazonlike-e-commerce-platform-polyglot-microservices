package com.nguyenhien.auth_service.application.event.message;

import java.time.Instant;
import java.util.UUID;

public record UserRegisterEvent (
    UUID eventId,

    UUID authUserId,

    String email,

    Instant occurredAt
) {
    
}
