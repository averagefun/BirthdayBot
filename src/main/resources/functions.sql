-- 1. Функция регистрации пользователя
CREATE OR REPLACE FUNCTION register_user(p_user_id BIGINT, p_username VARCHAR, p_lang VARCHAR) RETURNS VOID AS
$$
BEGIN
    INSERT INTO users (id, username, timezone, status, lang)
    VALUES (p_user_id, p_username, NULL, 0, p_lang);
END;
$$ LANGUAGE plpgsql;

-- 2. Функция обновления часового пояса пользователя
CREATE OR REPLACE FUNCTION update_timezone(p_user_id BIGINT, p_timezone INTEGER) RETURNS VOID AS
$$
BEGIN
    UPDATE users SET timezone = p_timezone WHERE id = p_user_id;
END;
$$ LANGUAGE plpgsql;

-- 3. Добавление дня рождения
CREATE OR REPLACE FUNCTION add_birthday(p_name VARCHAR, p_date DATE, p_owner_id BIGINT) RETURNS VOID AS
$$
BEGIN
    INSERT INTO birthdays (name, date, owner_id) VALUES (p_name, p_date, p_owner_id);
END;
$$ LANGUAGE plpgsql;

-- 4. Показать все дни рождения в заданном месяце
CREATE OR REPLACE FUNCTION show_birthdays(p_owner_id BIGINT, p_month INTEGER)
    RETURNS TABLE
            (
                birthday_id BIGINT,
                name        VARCHAR,
                date        DATE
            )
AS
$$
BEGIN
    RETURN QUERY SELECT b.id AS birthday_id, b.name, b.date
                 FROM birthdays b
                 WHERE b.owner_id = p_owner_id
                   AND EXTRACT(MONTH FROM b.date) = p_month
                 ORDER BY b.date;
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION show_birthdays(BIGINT, INTEGER) OWNER TO postgres;


-- 5. Обновить share_code пользователя
CREATE OR REPLACE FUNCTION update_share_code(p_user_id BIGINT) RETURNS VOID AS
$$
BEGIN
    UPDATE users SET share_code = FLOOR(RANDOM() * 100000) WHERE id = p_user_id;
END;
$$ LANGUAGE plpgsql;

-- 6. Создать запись о группе
CREATE OR REPLACE FUNCTION create_group(p_group_id BIGINT) RETURNS VOID AS
$$
BEGIN
    INSERT INTO groups (id, time_zone, is_bot_admin) VALUES (p_group_id, NULL, FALSE) ON CONFLICT (id) DO NOTHING;
END;
$$ LANGUAGE plpgsql;

-- 7. Удалить группу
CREATE OR REPLACE FUNCTION delete_group(p_group_id BIGINT) RETURNS VOID AS
$$
BEGIN
    DELETE FROM groups WHERE id = p_group_id;
END;
$$ LANGUAGE plpgsql;

-- 8. Получить список уведомлений
CREATE OR REPLACE FUNCTION get_ready_alarms()
    RETURNS TABLE
            (
                user_id    BIGINT,
                timezone   INTEGER,
                birth_date DATE,
                birth_boy  VARCHAR,
                alarm_id   BIGINT
            )
AS
$$
BEGIN
    RETURN QUERY SELECT u.id, u.timezone, b.date, b.name, a.id
                 FROM users AS u
                          JOIN birthdays AS b ON u.id = b.owner_id
                          JOIN alarms AS a ON b.id = a.birthday_id
                 WHERE (NOW() AT TIME ZONE 'UTC' + INTERVAL '1 hour' * u.timezone) >= a.time
                 ORDER BY a.time
                 LIMIT 100;
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION get_ready_alarms() OWNER TO postgres;


-- 9. Функция для получения списка пользователей
CREATE OR REPLACE FUNCTION get_users()
    RETURNS TABLE
            (
                user_id    BIGINT,
                username   VARCHAR,
                timezone   INTEGER,
                status     SMALLINT,
                lang       VARCHAR,
                share_code BIGINT
            )
AS
$$
BEGIN
    RETURN QUERY SELECT u.id AS user_id, u.username, u.timezone, u.status, u.lang, u.share_code
                 FROM users u
                 ORDER BY u.id;
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION get_users() OWNER TO postgres;

-- 10. Функция для удаления пользователя по идентификатору
CREATE OR REPLACE FUNCTION delete_user(p_user_id BIGINT) RETURNS VOID AS
$$
BEGIN
    DELETE FROM users WHERE id = p_user_id;
END;
$$ LANGUAGE plpgsql;

-- 11. Процедура для обновления языка пользователя
CREATE OR REPLACE FUNCTION update_language(p_user_id BIGINT, p_new_lang VARCHAR) RETURNS VOID AS
$$
BEGIN
    UPDATE users SET lang = p_new_lang WHERE id = p_user_id;
END;
$$ LANGUAGE plpgsql;

-- 12. Функция для получения дней рождения пользователя по идентификатору
CREATE OR REPLACE FUNCTION get_user_birthdays(p_user_id BIGINT)
    RETURNS TABLE
            (
                birthday_id BIGINT,
                name        VARCHAR,
                date        DATE,
                owner_id    BIGINT,
                group_id    BIGINT
            )
AS
$$
BEGIN
    RETURN QUERY SELECT b.id AS birthday_id, b.name, b.date, b.owner_id, b.group_id
                 FROM birthdays b
                 WHERE b.owner_id = p_user_id
                 ORDER BY b.date;
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION get_user_birthdays(BIGINT) OWNER TO postgres;

-- 13. Функция для получения списка групп
CREATE OR REPLACE FUNCTION get_groups()
    RETURNS TABLE
            (
                group_id     BIGINT,
                time_zone    INTEGER,
                is_bot_admin BOOLEAN
            )
AS
$$
BEGIN
    RETURN QUERY SELECT g.id AS group_id, g.time_zone, g.is_bot_admin
                 FROM groups g
                 ORDER BY g.id;
END;
$$ LANGUAGE plpgsql;

ALTER FUNCTION get_groups() OWNER TO postgres;

-- 14. Процедура для обновления пояса времени группы по идентификатору
CREATE OR REPLACE FUNCTION update_group_timezone(p_group_id BIGINT, p_new_timezone INTEGER) RETURNS VOID AS
$$
BEGIN
    UPDATE groups SET time_zone = p_new_timezone WHERE id = p_group_id;
END;
$$ LANGUAGE plpgsql;

-- 15. Функция для получения списка уведомлений по идентификатору пользователя и дате дня рождения
CREATE OR REPLACE FUNCTION get_birthday_alarms(p_owner_id BIGINT, p_birthday_date DATE)
    RETURNS TABLE
            (
                id          BIGINT,
                "time"      TIMESTAMP,
                birthday_id BIGINT
            )
AS
$$
BEGIN
    RETURN QUERY SELECT a.id, a."time", a.birthday_id
                 FROM alarms as a
                          JOIN birthdays as b ON a.birthday_id = b.id
                 WHERE b.owner_id = p_owner_id
                   AND b.date = p_birthday_date
                 ORDER BY a."time";
END;
$$ LANGUAGE plpgsql;

