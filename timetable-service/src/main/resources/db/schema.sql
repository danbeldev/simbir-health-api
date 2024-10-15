CREATE TABLE timetables
(
    id          BIGINT generated always as identity,
    "from"      timestamp             not null,
    "to"        timestamp             not null,
    doctor_id   BIGINT                not null,
    hospital_id BIGINT                not null,
    room_name   varchar(48)           not null,
    is_deleted  BOOLEAN DEFAULT FALSE NOT NULL,

    CONSTRAINT PK__timetables__key PRIMARY KEY (id),
    CONSTRAINT FK__timetables__doctor FOREIGN KEY (doctor_id) REFERENCES users (id),
    CONSTRAINT FK__timetables__room FOREIGN KEY (room_name, hospital_id) REFERENCES hospital_rooms (name, hospital_id)
);

CREATE TABLE timetable_appointments
(
    id           BIGINT generated always as identity,
    timetable_id BIGINT                not null,
    user_id      BIGINT                not null,
    time         timestamp             not null,
    is_deleted   BOOLEAN DEFAULT FALSE NOT NULL,

    CONSTRAINT PK__appointments__key PRIMARY KEY (id),
    CONSTRAINT FK__appointments__timetable FOREIGN KEY (timetable_id) REFERENCES timetables (id),
    CONSTRAINT FK__appointments__user FOREIGN KEY (user_id) REFERENCES users (id)
);