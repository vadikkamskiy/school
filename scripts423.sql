SELECT 
    s.name AS student_name,
    s.age AS student_age,
    f.name AS faculty_name
FROM 
    student s
INNER JOIN 
    faculty f ON s.faculty = f.id;


SELECT 
    s.name AS student_name,
    s.age AS student_age,
    a.id AS avatar_id
FROM 
    student s
INNER JOIN 
    avatar a ON a.student_id = s.id; -- или a.student_id = s.id в зависимости от названия колонки
