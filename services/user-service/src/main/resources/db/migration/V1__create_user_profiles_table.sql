CREATE TABLE user_profiles (
    id UUID PRIMARY KEY,

    auth_user_id UUID UNIQUE NOT NULL,

    email VARCHAR(255) UNIQUE NOT NULL,

    full_name VARCHAR(255),

    phone VARCHAR(50),

    gender VARCHAR(20),

    birthday DATE,

    avatar_url TEXT,

    status VARCHAR(30) NOT NULL,

    profile_completed BOOLEAN DEFAULT FALSE,

    loyalty_point BIGINT DEFAULT 0,

    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);