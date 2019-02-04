package com.geekbrains.refflection;

@Table(title = "students")
public class Student {

    public Student(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    @Column
    int id;

    @Column
    String name;

    @Column
    int score;

}
