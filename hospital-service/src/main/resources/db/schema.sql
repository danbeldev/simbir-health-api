CREATE TABLE hospitals
(
    id            BIGINT generated always as identity,
    name          varchar(128) not null,
    address       varchar(256) not null,
    contact_phone VARCHAR(15)  not null,
    is_deleted    boolean      not null default false,

    CONSTRAINT PK__hospitals__key PRIMARY KEY (id)
);

CREATE TABLE hospital_rooms
(
    id          BIGINT generated always as identity,
    name        varchar(48) unique not null,
    hospital_id BIGINT         not null,

    CONSTRAINT PK__hospital_rooms__key PRIMARY KEY (id),
    CONSTRAINT FK__hospital_rooms__hospital FOREIGN KEY (hospital_id) REFERENCES hospitals (id)
);

CREATE TABLE timetables
(
    id          BIGINT generated always as identity,
    "from"      timestamp not null,
    "to"        timestamp not null,
    doctor_id   BIGINT       not null,
    hospital_id BIGINT       not null,
    room_id     BIGINT       not null,

    CONSTRAINT PK__timetables__key PRIMARY KEY (id),
    CONSTRAINT FK__timetables__doctor FOREIGN KEY (doctor_id) REFERENCES users (id),
    CONSTRAINT FK__timetables__hospital FOREIGN KEY (hospital_id) REFERENCES hospitals (id),
    CONSTRAINT FK__timetables__room FOREIGN KEY (room_id) REFERENCES hospital_rooms (id)
);