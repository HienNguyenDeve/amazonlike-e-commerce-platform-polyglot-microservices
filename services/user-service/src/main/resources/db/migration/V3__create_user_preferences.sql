CREATE TABLE user_preferences (
    id UUID PRIMARY KEY,

    user_profile_id UUID UNIQUE NOT NULL,

    language VARCHAR(20) DEFAULT 'vi',

    currency VARCHAR(10) DEFAULT 'VND',

    email_notification BOOLEAN DEFAULT TRUE,

    sms_notification BOOLEAN DEFAULT FALSE,

    push_notification BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL
);