package com.geekbrains.refflection;

public class MyTest {

    @Anno(description = "Я запускаю тест №1")
    public static void test1(){
        System.out.println("test1");
    }

    @Anno
    public static void test2(){
        System.out.println("test2");
    }

    @Anno(description = "Я запускаю тест №1")
    public static void test3(){
        System.out.println("test3");
    }
}
