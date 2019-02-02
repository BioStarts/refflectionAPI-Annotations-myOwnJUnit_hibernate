package com.geekbrains.refflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MainApp {
    public static void main(String[] args) throws Exception{
        //пример реализации своего мини JUnit :
        Class mt = MyTest.class; //складываем в объект наш помеченный аннотациями класс
        Method[] methods = mt.getDeclaredMethods();// получаем методы класса
        for (Method o : methods
             ) {
            if (o.isAnnotationPresent(Anno.class)){ //запускаем методы класса которые помеченны аннотацией
                System.out.println("Описание запускаемого метода: " + o.getAnnotation(Anno.class).description()); // достаем описание запускаемого метода
                o.invoke(null);
            }
        }
    }


    //базовые примеры использования refflectionAPI
    public static void reflectionBasicCode() throws Exception {
        Class c1 = Cat.class;// 3 способа создания объекта типа Класс
        Class c2 = new Cat("Barsic", 10, "black").getClass();
        Class c3 = Class.forName("com.geekbrains.refflection.Cat");


        Constructor constr = c1.getConstructor(String.class, int.class, String.class);//решили создать кота другим способом,
        // для этого создаем конструктор класса кэт
        Cat cat = (Cat) constr.newInstance("Bobik", 5, "White");//и создаем с его помощью кота
        cat.info();//проверяем удалось ли

        Method[] methods = c1.getMethods(); // при получении списка методов класса таким образом получаем только список открытых public методов
        for (Method o: methods) {
            System.out.println(o);
        }

        Method[] methodsAll = c1.getDeclaredMethods(); // получаем список всех методов даже закрытх модификаторами доступа
        for (Method o: methodsAll) {
            System.out.println(o);
        }

        Method mInfo = c1.getDeclaredMethod("info"); //для вызова метода инфо получаем на него ссылку
        mInfo.invoke(cat);//вызываем метод у объекта


        int mods = mInfo.getModifiers();// узнать хотим модификатор доступа нашего метода
        System.out.println(Modifier.isAbstract(mods));
        System.out.println(Modifier.isPublic(mods));
        System.out.println(Modifier.isStatic(mods));

        Field field = c1.getField("name");//для работы с полями объекта получаем к нему доступ
        System.out.println(field.get(cat));//спрашиваем значение поля имя у нашего кота

        Field field2 = c1.getDeclaredField("color");//для работы с Приватными полями объекта
        field2.setAccessible(true);//даем себе доступ к полям с приватным модификаторам
        System.out.println(field2.get(cat));//спрашиваем значение приватного поля цвет у нашего кота
        field2.set(cat, "Purple");//внимание! меняем значение поля с закрытым модификатором доступа (а именно цвет на фиолетовый)
        cat.info();
    }
}
