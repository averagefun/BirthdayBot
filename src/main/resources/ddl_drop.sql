drop table support_statuses;

drop table texts;

drop table alarms;

drop table birthdays;

drop table member_permissions;

drop table group_members;

drop table users;

drop table groups;

drop table supports;

drop function register_user(bigint, varchar, varchar);

drop function update_timezone(bigint, integer);

drop function add_birthday(varchar, date, bigint);

drop function show_birthdays(bigint, integer);

drop function update_share_code(bigint);

drop function create_group(bigint);

drop function delete_group(bigint);

drop function get_ready_alarms();

drop function get_users();

drop function delete_user(bigint);

drop function update_language(bigint, varchar);

drop function get_user_birthdays(bigint);

drop function get_groups();

drop function update_group_timezone(bigint, integer);

drop function get_birthday_alarms(bigint, date);

drop sequence custom_birthdays_seq;

drop sequence custom_alarms_seq;

drop sequence custom_group_members_seq;

drop sequence supports_seq;

