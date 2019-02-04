package com.geekbrains.refflection.trash;

public class Cat  {// создаем простой класс с набором полей и методов с разными модификаторами доступа
    // и поиграем с ними с помощью refflectionAPI
    public String name;
    int age;
    private String color;

    public Cat(String name, int age, String color) {
        this.name = name;
        this.age = age;
        this.color = color;
    }

    public void info(){
        System.out.println(name + " " + color + " " + age);
    }

    private void meow(){
        System.out.println(name + " meow");
    }

}
