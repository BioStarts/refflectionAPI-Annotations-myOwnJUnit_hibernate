package com.geekbrains.refflection;

@Table(title = "teachers")
public class Teachers {

    public Teachers(int id, String name, String grade) {
        this.id = id;
        this.name = name;
        this.grade = grade;
    }

    @Column
    int id;

    @Column
    String name;

    @Column
    String grade;
}
