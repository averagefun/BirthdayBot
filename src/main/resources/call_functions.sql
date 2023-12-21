-- Вызов первой функции
SELECT register_user(223, 'user14', 'en');
-- Вызов второй функции
SELECT update_timezone(1, 5);

-- Вызов третьей функции
SELECT add_birthday('Birthday1', '2023-01-01', 1);
-- Вызов четвёртой функции
SELECT show_birthdays(1, 1);

-- Вызов пятой функции
SELECT update_share_code(1);
-- Вызов шестой функции
SELECT create_group(1);

-- Вызов седьмой функции
SELECT delete_group(2);
-- Вызов восьмой функции
SELECT get_ready_alarms();

-- Вызов девятой функции
SELECT get_users();

-- Вызов десятой функции
SELECT delete_user(223);

-- Вызов одиннадцатой функции
SELECT update_language(1, 'ru');
-- Вызов двенадцатой функции
SELECT get_user_birthdays(1);

-- Вызов тринадцатой функции
SELECT get_groups();
-- Вызов четырнадцатой функции
SELECT update_group_timezone(1, 3);

-- Вызов пятнадцатой функции
SELECT get_birthday_alarms(1, '2023-01-01');

