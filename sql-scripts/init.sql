CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE IF NOT EXISTS users
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY NOT NULL,
    last_name  VARCHAR(64)                         NOT NULL,
    first_name VARCHAR(64)                         NOT NULL,
    username   VARCHAR(64)                         NOT NULL,
    password   VARCHAR(128)                        NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE               NOT NULL,
    CONSTRAINT PK__users__key PRIMARY KEY (id)
    );

-- DO
-- $$
-- BEGIN
--         IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'user_role') THEN
-- CREATE TYPE user_role AS ENUM ('User', 'Doctor', 'Manager', 'Admin');
-- END IF;
-- END
-- $$;

CREATE TABLE IF NOT EXISTS user_roles
(
    user_id BIGINT    NOT NULL,
    role_id varchar(16) NOT NULL,
    CONSTRAINT PK__user_roles__key PRIMARY KEY (user_id, role_id)
    );

INSERT INTO users (last_name, first_name, username, password, is_deleted)
VALUES
    ('user', 'user', 'user', crypt('user', gen_salt('bf')), FALSE),
    ('doctor', 'doctor', 'doctor', crypt('doctor', gen_salt('bf')), FALSE),
    ('manager', 'manager', 'manager', crypt('manager', gen_salt('bf')), FALSE),
    ('admin', 'admin', 'admin', crypt('admin', gen_salt('bf')), FALSE);

INSERT INTO user_roles (user_id, role_id)
VALUES
    ((SELECT id FROM users WHERE username = 'user'), 'User'),
    ((SELECT id FROM users WHERE username = 'doctor'), 'Doctor'),
    ((SELECT id FROM users WHERE username = 'manager'), 'Manager'),
    ((SELECT id FROM users WHERE username = 'admin'), 'Admin');


CREATE TABLE IF NOT EXISTS active_tokens
(
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    CONSTRAINT PK__active_tokens_key PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS hospitals
(
    id            BIGINT GENERATED ALWAYS AS IDENTITY,
    name          VARCHAR(128) NOT NULL,
    address       VARCHAR(256) NOT NULL,
    contact_phone VARCHAR(15)  NOT NULL,
    is_deleted    BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT PK__hospitals__key PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS hospital_rooms
(
    name        VARCHAR(48) NOT NULL,
    hospital_id BIGINT      NOT NULL,
    CONSTRAINT PK__hospital_rooms__key PRIMARY KEY (name, hospital_id),
    CONSTRAINT FK__hospital_rooms__hospital FOREIGN KEY (hospital_id) REFERENCES hospitals (id)
    );

CREATE TABLE IF NOT EXISTS timetables
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY,
    "from"      TIMESTAMP             NOT NULL,
    "to"        TIMESTAMP             NOT NULL,
    doctor_id   BIGINT                NOT NULL,
    hospital_id BIGINT                NOT NULL,
    room_name   VARCHAR(48)           NOT NULL,
    is_deleted  BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT PK__timetables__key PRIMARY KEY (id),
    CONSTRAINT FK__timetables__doctor FOREIGN KEY (doctor_id) REFERENCES users (id),
    CONSTRAINT FK__timetables__room FOREIGN KEY (room_name, hospital_id) REFERENCES hospital_rooms (name, hospital_id)
    );

CREATE TABLE IF NOT EXISTS timetable_appointments
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY,
    timetable_id BIGINT                NOT NULL,
    user_id      BIGINT                NOT NULL,
    time         TIMESTAMP             NOT NULL,
    is_deleted   BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT PK__appointments__key PRIMARY KEY (id),
    CONSTRAINT FK__appointments__timetable FOREIGN KEY (timetable_id) REFERENCES timetables (id),
    CONSTRAINT FK__appointments__user FOREIGN KEY (user_id) REFERENCES users (id)
    );

CREATE TABLE IF NOT EXISTS histories
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY,
    pacient_id  BIGINT        NOT NULL,
    hospital_id BIGINT        NOT NULL,
    doctor_id   BIGINT        NOT NULL,
    room_name   VARCHAR(48)   NOT NULL,
    data        VARCHAR(2048) NOT NULL,
    date        TIMESTAMP     NOT NULL,
    CONSTRAINT PK__histories__key PRIMARY KEY (id),
    CONSTRAINT FK__histories__doctor FOREIGN KEY (doctor_id) REFERENCES users (id),
    CONSTRAINT FK__histories__pacient FOREIGN KEY (pacient_id) REFERENCES users (id),
    CONSTRAINT FK__histories__room FOREIGN KEY (room_name, hospital_id) REFERENCES hospital_rooms (name, hospital_id)
    );
