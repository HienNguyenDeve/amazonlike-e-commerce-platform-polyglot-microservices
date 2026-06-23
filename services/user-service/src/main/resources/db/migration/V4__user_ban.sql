CREATE TABLE user_ban_history (
    id UUID PRIMARY KEY,

    user_profile_id UUID NOT NULL,

    reason TEXT,

    banned_by UUID,

    banned_at TIMESTAMPTZ NOT NULL
);