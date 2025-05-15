ALTER TABLE student
    ADD CONSTRAINT chk_student_age CHECK (age >= 16) NOT VALID;

ALTER TABLE student
    ALTER COLUMN name SET NOT NULL;

ALTER TABLE student
    ADD CONSTRAINT uq_student_name UNIQUE (name);

ALTER TABLE faculty
    ADD CONSTRAINT uq_faculty_name_color UNIQUE (name, color);

ALTER TABLE student
    ALTER COLUMN age SET DEFAULT 20;