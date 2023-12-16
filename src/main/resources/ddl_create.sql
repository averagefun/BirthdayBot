CREATE TABLE users
(
    id         BIGINT       NOT NULL,
    username   VARCHAR(255) NOT NULL UNIQUE,
    timezone   INTEGER CHECK (timezone >= -12 AND timezone <= 12),
    status     SMALLINT DEFAULT 0,
    lang       VARCHAR(255),
    share_code BIGINT,
    CONSTRAINT pk_users PRIMARY KEY (id)
);
CREATE INDEX i_status_index ON users (status);


CREATE TABLE support_statuses
(
    id         BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    updated_at date         NOT NULL,
    CONSTRAINT pk_supportstatus PRIMARY KEY (id)
);


CREATE TABLE texts
(
    tag         VARCHAR(255) NOT NULL,
    language_id BIGINT       NOT NULL,
    content     VARCHAR(255),
    CONSTRAINT pk_texts PRIMARY KEY (tag, language_id)
);


CREATE TABLE groups
(
    id           BIGINT NOT NULL,
    time_zone    INTEGER,
    is_bot_admin BOOLEAN,
    CONSTRAINT pk_groups PRIMARY KEY (id)
);


CREATE SEQUENCE IF NOT EXISTS custom_birthdays_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE birthdays
(
    id       BIGINT       DEFAULT NEXTVAL('custom_birthdays_seq') NOT NULL PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    date     DATE         NOT NULL,
    owner_id BIGINT       NOT NULL,
    group_id BIGINT,
    CONSTRAINT FK_BIRTHDAY_ON_GROUP FOREIGN KEY (group_id) REFERENCES groups (id),
    CONSTRAINT FK_BIRTHDAY_ON_OWNER FOREIGN KEY (owner_id) REFERENCES users (id),
    UNIQUE (name, owner_id)
);
CREATE INDEX i_date_index ON birthdays (date);


CREATE SEQUENCE IF NOT EXISTS custom_alarms_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE alarms
(
    id          BIGINT DEFAULT NEXTVAL('custom_alarms_seq') NOT NULL PRIMARY KEY,
    time        TIMESTAMP   NOT NULL,
    birthday_id BIGINT,
    CONSTRAINT FK_ALARM_ON_BIRTHDAY FOREIGN KEY (birthday_id) REFERENCES birthdays (id)
);
CREATE INDEX i_alarm_time_index ON alarms (time);

CREATE SEQUENCE IF NOT EXISTS custom_group_members_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE group_members
(
    id       BIGINT DEFAULT NEXTVAL('custom_group_members_seq') NOT NULL PRIMARY KEY,
    user_id  BIGINT,
    group_id BIGINT,
    CONSTRAINT FK_GROUPMEMBER_ON_GROUP FOREIGN KEY (group_id) REFERENCES groups (id),
    CONSTRAINT FK_GROUPMEMBER_ON_USER FOREIGN KEY (user_id) REFERENCES users (id)
);


CREATE TABLE member_permissions
(
    member_id  BIGINT NOT NULL,
    permission SMALLINT NOT NULL,
    CONSTRAINT FK_MEMBERPERMISSION_ON_GROUPMEMBER FOREIGN KEY (member_id) REFERENCES group_members (id),
    PRIMARY KEY (member_id, permission)
);


CREATE SEQUENCE IF NOT EXISTS supports_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE supports
(
    id         BIGINT DEFAULT NEXTVAL('supports_seq') NOT NULL PRIMARY KEY,
    content    VARCHAR(255) NOT NULL,
    priority   INTEGER      NOT NULL,
    status     SMALLINT     NOT NULL DEFAULT 0
);
