INSERT INTO users (last_name, first_name, username, password, is_deleted)
VALUES ('Иванов', 'Иван', 'ivanov', 'password123', FALSE),
       ('Петрова', 'Анна', 'petrova', 'password456', FALSE),
       ('Сидоров', 'Алексей', 'sidorov', 'password789', FALSE),
       ('Кузнецова', 'Мария', 'kuznetsova', 'password321', FALSE),
       ('Смирнов', 'Дмитрий', 'smirnov', 'password654', FALSE);

INSERT INTO active_tokens (id)
VALUES (gen_random_uuid()),
       (gen_random_uuid()),
       (gen_random_uuid()),
       (gen_random_uuid());

INSERT INTO user_roles (user_id, role)
VALUES (1, 'User'),
       (1, 'Doctor'),
       (2, 'Manager'),
       (3, 'Admin'),
       (4, 'User'),
       (5, 'Doctor');