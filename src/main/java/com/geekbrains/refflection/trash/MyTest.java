package com.geekbrains.refflection.trash;

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

    @BeforeSuite(description = "Я запускаю BeforeSuite тест")
    public static void test4(){
        System.out.println("BeforeSuite");
    }

    @AfterSuite(description = "Я запускаю AfterSuite тест")
    public static void test5(){
        System.out.println("AfterSuite");
    }

    @Test()
    public static void test6(){
        System.out.println("Test6 5 priority");
    }

    @Test(priority = 4)
    public static void test7(){
        System.out.println("Test7 4 priority");
    }

    @Test(priority = 4)
    public static void test8(){
        System.out.println("Test8 4 priority");
    }




}
