select * from student;

select * from student
where age>10 and age<20;

select s.name from student as s;

select * from student
where name like '%о%';

select * from student as s
where s.id < 5;

select * from student as s
order by s.age asc;