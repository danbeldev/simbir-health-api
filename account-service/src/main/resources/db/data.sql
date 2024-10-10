-- Вставляем тестовые данные в таблицу users
INSERT INTO users (last_name, first_name, username, password, is_deleted)
VALUES ('Иванов', 'Иван', 'ivanov', 'password123', FALSE),
       ('Петров', 'Петр', 'petrov', 'password123', FALSE),
       ('Сидоров', 'Сидор', 'sidorov', 'password123', FALSE),
       ('Смирнова', 'Анна', 'smirnova', 'password123', FALSE),
       ('Кузнецов', 'Алексей', 'kuznetsov', 'password123', FALSE);

-- Вставляем тестовые данные в таблицу active_tokens
INSERT INTO active_tokens (id)
VALUES (gen_random_uuid()),
       (gen_random_uuid()),
       (gen_random_uuid());

-- Вставляем тестовые данные в таблицу user_roles
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 'Admin'),
       (2, 'Doctor'),
       (3, 'User'),
       (4, 'Manager'),
       (5, 'User');