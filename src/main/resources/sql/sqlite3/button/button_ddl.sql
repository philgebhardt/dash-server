DROP TABLE IF EXISTS BUTTON_EVENT;
DROP TABLE IF EXISTS BUTTON;

CREATE TABLE BUTTON(
    id CHAR(36) PRIMARY KEY NOT NULL,
    name CHAR(36) NOT NULL
);

CREATE TABLE BUTTON_EVENT(
    id CHAR(36) NOT NULL,
    dttm_occurred DATETIME NOT NULL,
    FOREIGN KEY(id) REFERENCES button(id)
);