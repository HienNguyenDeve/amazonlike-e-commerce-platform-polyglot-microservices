CREATE TABLE addresses (
    id UUID PRIMARY KEY,

    user_profile_id UUID NOT NULL,

    receiver_name VARCHAR(255) NOT NULL,

    phone VARCHAR(50) NOT NULL,

    province VARCHAR(100) NOT NULL,

    district VARCHAR(100) NOT NULL,

    ward VARCHAR(100) NOT NULL,

    detail_address TEXT NOT NULL,

    postal_code VARCHAR(20),

    address_type VARCHAR(30),

    is_default BOOLEAN DEFAULT FALSE,

    created_at TIMESTAMPTZ NOT NULL,

    updated_at TIMESTAMPTZ NOT NULL
);