package ru.hogwarts.school.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Student{

    @Id
    @GeneratedValue
    private long id;

    private String name;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "faculty_id" )
    private Faculty faculty;

    @Override
    public String toString(){
        return "Faculty{" +
                "id=" + id + '\'' +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals( Object o ){
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return age == student.age && Objects.equals( name, student.name );
    }

    @Override
    public int hashCode(){
        return Objects.hash( name, age );
    }

    public long getId(){
        return id;
    }

    public void setId( long id ){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName( String name ){
        this.name = name;
    }

    public int getAge(){
        return age;
    }

    public void setAge( int age ){
        this.age = age;
    }

    public Faculty getFaculty(){
        if (faculty == null) {
            return new Faculty();
        } else {
            return faculty;
        }
    }

    public void setFaculty( Faculty faculty ){
        if (faculty == null) {
            this.faculty = new Faculty();
        } else {
            this.faculty = faculty;
        }
    }
}