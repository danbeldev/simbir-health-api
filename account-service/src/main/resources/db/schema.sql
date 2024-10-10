CREATE
EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE users
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    last_name  VARCHAR(64)                         NOT NULL,
    first_name VARCHAR(64)                         NOT NULL,
    username   VARCHAR(64)                         NOT NULL,
    password   VARCHAR(128)                        NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE               NOT NULL,
    CONSTRAINT PK__users__key PRIMARY KEY (id)
);

CREATE TABLE active_tokens
(
    id UUID NOT NULL DEFAULT gen_random_uuid(),

    CONSTRAINT PK__active_tokens_key PRIMARY KEY (id)
);

CREATE TYPE user_role AS ENUM ('User', 'Doctor', 'Manager', 'Admin');

CREATE TABLE user_roles
(
    user_id BIGINT    not null,
    role_id user_role not null,

    CONSTRAINT PK__user_roles__key PRIMARY KEY (user_id, role_id)
);
