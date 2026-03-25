-- liquibase formatted sql

-- changeset Ivan:1
CREATE TABLE if not exists student (
                                       id SERIAL,
                                       name TEXT,
                                       age INTEGER
);


-- changeset Ivan:2
CREATE TABLE if not exists faculty (
                                       id SERIAL,
                                       name TEXT,
                                       color text
);

-- changeset Ivan:3
CREATE INDEX IF NOT EXISTS faculty_name_color_index ON faculty (name, color);

-- changeset Ivan:4
CREATE INDEX IF NOT EXISTS student_name_index ON student (name);