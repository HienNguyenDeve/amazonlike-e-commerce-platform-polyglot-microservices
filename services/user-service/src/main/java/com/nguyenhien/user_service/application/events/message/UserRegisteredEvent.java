package com.nguyenhien.user_service.application.events.message;

import java.time.Instant;
import java.util.UUID;

public record UserRegisteredEvent(
    UUID eventId, UUID authUserId, String email, Instant occurredAt) {}
