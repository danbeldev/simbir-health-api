CREATE TABLE histories
(
    id          bigint generated always as identity,
    pacient_id  bigint        not null,
    hospital_id bigint        not null,
    doctor_id   bigint        not null,
    room_name   varchar(48)   not null,
    data        varchar(2048) not null,
    date        timestamp     not null,

    CONSTRAINT PK__histories__key PRIMARY KEY (id),
    CONSTRAINT FK__histories__doctor FOREIGN KEY (doctor_id) REFERENCES users (id),
    CONSTRAINT FK__histories__pacient FOREIGN KEY (pacient_id) REFERENCES users (id),
    CONSTRAINT FK__histories__room FOREIGN KEY (room_name, hospital_id) REFERENCES hospital_rooms (name, hospital_id)
);