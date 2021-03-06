DROP TABLE IF EXISTS BUTTON_EVENT;
DROP TABLE IF EXISTS NEW_BUTTON_EVENT;
DROP TABLE IF EXISTS BUTTON;
DROP TABLE IF EXISTS IGNORE_BUTTON;
DROP TABLE IF EXISTS NEW_BUTTON;

CREATE TABLE BUTTON(
    id CHAR(36) PRIMARY KEY NOT NULL,
    name CHAR(36) NOT NULL
);

CREATE TABLE IGNORE_BUTTON(
    id CHAR(36) PRIMARY KEY NOT NULL,
    name CHAR(36) NOT NULL
);

CREATE TABLE NEW_BUTTON(
    id CHAR(36) PRIMARY KEY NOT NULL,
    name CHAR(36) NOT NULL
);

CREATE TABLE BUTTON_EVENT(
    id CHAR(36) NOT NULL,
    dttm_occurred DATETIME NOT NULL,
    FOREIGN KEY(id) REFERENCES button(id)
);

CREATE TABLE NEW_BUTTON_EVENT(
    id CHAR(36) NOT NULL,
    dttm_occurred DATETIME NOT NULL,
    FOREIGN KEY(id) REFERENCES button(id)
);