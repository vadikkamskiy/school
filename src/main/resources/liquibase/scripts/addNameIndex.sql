--liquibase formatted sql

--changeset vadikkamskiy:1
CREATE INDEX IF NOT EXISTS student_name_index ON student(name);