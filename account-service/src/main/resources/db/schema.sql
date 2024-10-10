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

CREATE TABLE hospitals
(
    id            int generated always as identity,
    name          varchar(128) not null,
    address       varchar(256) not null,
    contact_phone VARCHAR(15)  not null,
    is_deleted    boolean      not null default false,

    CONSTRAINT PK__hospitals__key PRIMARY KEY (id)
);

CREATE TABLE hospital_rooms
(
    id          int generated always as identity,
    name        varchar(48) not null,
    hospital_id int         not null,

    CONSTRAINT PK__hospital_rooms__key PRIMARY KEY (id),
    CONSTRAINT FK__hospital_rooms__hospital FOREIGN KEY (hospital_id) REFERENCES hospitals (id)
);

CREATE TABLE timetables
(
    id          int generated always as identity,
    "from"      timestamp not null,
    "to"        timestamp not null,
    doctor_id   int       not null,
    hospital_id int       not null,
    room_id     int       not null,

    CONSTRAINT PK__timetables__key PRIMARY KEY (id),
    CONSTRAINT FK__timetables__doctor FOREIGN KEY (doctor_id) REFERENCES users (id),
    CONSTRAINT FK__timetables__hospital FOREIGN KEY (hospital_id) REFERENCES hospitals (id),
    CONSTRAINT FK__timetables__room FOREIGN KEY (room_id) REFERENCES hospital_rooms (id)
);