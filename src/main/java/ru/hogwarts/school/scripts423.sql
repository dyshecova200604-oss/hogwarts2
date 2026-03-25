SELECT student.name, student.age, faculty.name FROM student
JOIN faculty ON faculty.id = student.faculty_id;

SELECT student.name, student.age FROM student
JOIN avatar ON avatar.student_id = student.id;