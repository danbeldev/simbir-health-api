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
    name        varchar(48) not null,
    hospital_id BIGINT      not null,

    CONSTRAINT PK__hospital_rooms__key PRIMARY KEY (name, hospital_id),
    CONSTRAINT FK__hospital_rooms__hospital FOREIGN KEY (hospital_id) REFERENCES hospitals (id)
);