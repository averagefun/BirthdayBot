/* Создаем триггер перед вставкой нового пользователя, который проверяет, равен ли статус пользователя 0.
   Если нет, выкидываем ошибку. */
CREATE OR REPLACE FUNCTION check_user_status_is_zero() RETURNS TRIGGER AS
$$
BEGIN
    IF NEW.status <> 0 THEN
        RAISE EXCEPTION 'User status should be 0 for user %', NEW.username;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_user_status_is_zero
    BEFORE INSERT
    ON users
    FOR EACH ROW
EXECUTE FUNCTION check_user_status_is_zero();


/* Создаем триггер перед вставкой новой записи в таблицу support, который проверяет значение поля status_id.
   Если статус не равен 0 и не равен 1, выкидываем ошибку. */
CREATE OR REPLACE FUNCTION check_support_status_is_zero_or_one() RETURNS TRIGGER AS
$$
BEGIN
    IF NOT (NEW.status = 0 OR NEW.status = 1) THEN
        RAISE EXCEPTION 'Support status should be 0 or 1 for support id %', NEW.id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_support_status_is_zero_or_one
    BEFORE INSERT
    ON supports
    FOR EACH ROW
EXECUTE FUNCTION check_support_status_is_zero_or_one();


/* Создаем триггер перед вставкой или обновлением alarm, data должна быть >= текущей */
CREATE OR REPLACE FUNCTION check_alarm_time()
    RETURNS TRIGGER AS $$
BEGIN
    IF (NEW.time < NOW()) THEN
        RAISE EXCEPTION 'The alarm time must be >= than current time';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_time_before_insert_or_update
    BEFORE INSERT OR UPDATE OF time
    ON alarms
    FOR EACH ROW
EXECUTE FUNCTION check_alarm_time();
