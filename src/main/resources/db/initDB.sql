DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS meals;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users
(
    id               INTEGER PRIMARY KEY DEFAULT NEXTVAL('global_seq'),
    name             VARCHAR                           NOT NULL,
    email            VARCHAR                           NOT NULL,
    password         VARCHAR                           NOT NULL,
    registered       TIMESTAMP           DEFAULT NOW() NOT NULL,
    enabled          bool                DEFAULT TRUE  NOT NULL,
    calories_per_day INTEGER             DEFAULT 2000  NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_role
(
    user_id INTEGER NOT NULL,
    role    VARCHAR NOT NULL,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE meals
(
    id          INTEGER PRIMARY KEY DEFAULT NEXTVAL('global_seq'),
    date_time   TIMESTAMP           DEFAULT NOW() NOT NULL,
    description TEXT,
    calories    INTEGER                           NOT NULL,
    user_id     INTEGER                           NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
CREATE INDEX meal_date_time_idx ON meals (date_time);
CREATE UNIQUE INDEX meal_id_date_time_idx ON meals (user_id, date_time);