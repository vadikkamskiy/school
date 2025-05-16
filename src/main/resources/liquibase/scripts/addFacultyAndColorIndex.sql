--liquibase formatted sql

--changeset vadikkamskiy:2
CREATE INDEX IF NOT EXISTS student_faculty_color_index ON faculty(name, color);
;
