INSERT INTO users (id, username, timezone, lang)
VALUES (1, 'testuser1', 5, 'en-US'),
       (2, 'testuser2', 3, 'en-US'),
       (3, 'testuser3', 3, 'en-US'),
       (4, 'testuser4', -1, 'es-ES'),
       (5, 'testuser5', 2, 'ru-RU');


INSERT INTO texts (tag, language_id, content)
VALUES ('ad', 1, 'This is adv'),
       ('info', 2, 'Small info'),
       ('cat', 3, 'myow');


INSERT INTO groups (id, time_zone, is_bot_admin)
VALUES (1, 5, true),
       (2, 3, false),
       (3, 3, true),
       (4, 2, false),
       (5, 1, true);


INSERT INTO birthdays (name, date, owner_id, group_id)
VALUES ('John', '1990-01-01', 1, null),
       ('Jane', '1995-02-15', 2, null),
       ('Alice', '2000-03-30', 3, null),
       ('James', '1970-11-01', 4, 5),
       ('Elena', '1975-10-05', 4, 5),
       ('Michael', '1981-06-23', 5, null),
       ('Ella', '1999-05-16', 1, null),
       ('Luna', '1986-04-22', 3, 4),
       ('Alex', '1982-07-19', 2, null),
       ('Noah', '1999-08-12', 1, null);


INSERT INTO alarms (time, birthday_id)
VALUES ('2023-01-01 16:00:00', 1),
       ('2023-02-15 00:00:00', 2),
       ('2023-03-30 00:00:00', 3),
       ('2023-11-01 00:00:00', 4),
       ('2023-10-05 00:00:00', 5);

INSERT INTO group_members (id, user_id, group_id)
VALUES (1, 1, null),
       (2, 2, null),
       (3, 3, 1),
       (4, 1, 1),
       (5, 2, null);

INSERT INTO member_permissions (member_id, permission)
VALUES (1, 0),
       (2, 0),
       (3, 1),
       (4, 2),
       (5, 2);

INSERT INTO supports (content, priority, status)
VALUES ('Sample support message 1', 1, 0),
       ('Sample support message 2', 2, 0),
       ('Sample support message 3', 3, 1),
       ('Sample support message 4', 1, 0),
       ('Sample support message 5', 2, 0);
