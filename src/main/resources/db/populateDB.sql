DELETE FROM user_role;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO meals(date_time, description, calories, user_id)
VALUES ('2023-10-01 10:00:00', 'Завтрак', 500, 100000),
       ('2023-10-01 13:00:00', 'Обед', 1000, 100000),
       ('2023-10-01 20:00:00', 'Ужин', 500, 100000),
       ('2023-10-15 00:00:00', 'Граничное значение', 100, 100000),
       ('2023-10-15 10:00:00', 'Завтрак', 1000, 100000),
       ('2023-10-15 14:00:00', 'Обед', 500, 100000),
       ('2023-10-15 19:00:00', 'Ужин', 410, 100000),
       ('2023-10-05 10:00:00', 'Завтрак админа', 1000, 100001),
       ('2023-10-05 13:00:00', 'Обед админа', 1500, 100001),
       ('2023-10-05 20:00:00', 'Ужин админа', 300, 100001);