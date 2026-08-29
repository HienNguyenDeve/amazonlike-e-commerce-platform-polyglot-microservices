package com.nguyenhien.user_service.application.events.message;

import java.time.Instant;
import java.util.UUID;

public record UserProfileCreatedEvent(
    UUID eventId, UUID userProfileId, UUID authUserId, String email, Instant occurredAt) {}
