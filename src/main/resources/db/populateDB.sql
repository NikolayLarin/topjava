DELETE
FROM user_roles;
DELETE
FROM meals;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_USER', 100000),
       ('ROLE_ADMIN', 100001);

INSERT INTO meals (date_time, description, calories, user_id)
VALUES ('2019-10-20 10:00', 'завтрак', 500, 100000),
       ('2019-10-20 13:00', 'обед', 1000, 100000),
       ('2019-10-20 19:00', 'ужин', 500, 100000),
       ('2019-10-21 10:00', 'завтрак', 500, 100000),
       ('2019-10-21 13:00', 'обед', 1000, 100000),
       ('2019-10-21 19:00', 'ужин', 510, 100000),
       ('2019-10-21 10:00', 'завтрак', 500, 100001),
       ('2019-10-21 13:00', 'обед', 1000, 100001),
       ('2019-10-21 19:00', 'ужин', 500, 100001);