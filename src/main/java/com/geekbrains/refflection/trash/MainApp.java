package com.geekbrains.refflection.trash;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class MainApp {

    public static void main(String[] args) throws Exception {
        Class ju = MyTest.class;
        start(ju);

        System.out.println("--------------Пример реализации своего мини JUnit попроще(по одной аннотации Anno):---------------");
        //пример реализации своего мини JUnit :
        Class mt = MyTest.class; //складываем в объект наш помеченный аннотациями класс
        Method[] methods = mt.getDeclaredMethods();// получаем методы класса
        for (Method o : methods
        ) {
            if (o.isAnnotationPresent(Anno.class)) { //запускаем методы класса которые помеченны аннотацией
                System.out.println("Описание запускаемого метода: " + o.getAnnotation(Anno.class).description()); // достаем описание запускаемого метода
                o.invoke(null);
            }
        }
    }

    /**
     Создать класс, который может выполнять «тесты». В качестве тестов выступают классы с наборами методов с аннотациями @Test.
     Для этого у него должен быть статический метод start(), которому в качестве параметра передается или объект типа Class, или имя класса.
     Из «класса-теста» вначале должен быть запущен метод с аннотацией @BeforeSuite, если такой имеется. Далее запущены методы с аннотациями @Test,
     а по завершении всех тестов – метод с аннотацией @AfterSuite. К каждому тесту необходимо добавить приоритеты (int числа от 1 до 10),
     в соответствии с которыми будет выбираться порядок их выполнения. Если приоритет одинаковый, то порядок не имеет значения.
     Методы с аннотациями @BeforeSuite и @AfterSuite должны присутствовать в единственном экземпляре,
     иначе необходимо бросить RuntimeException при запуске «тестирования».
    **/

    public static void start(Class ju) throws Exception {
        Method[] jMethods = ju.getDeclaredMethods();// получаем методы класса
        SortedSet<Integer> prioritySet = new TreeSet<Integer>();//Сет для формирования списка уникальных приоритетов методов помечнных аннотацией Test
        List<Integer> mProirity = new ArrayList<Integer>();//Лист для хранения списка приоритетов методов помечнных аннотацией Test, потом уники Set сложим в лист
        int b = 0;//счетчик для подсчета повторений аннотаций BeforeSuite в класе
        int a = 0;//счетчик для подсчета повторений аннотаций AfterSuite в класе
        for (Method o : jMethods) {
            if (o.isAnnotationPresent(BeforeSuite.class)) {
                b++;
            } else if (o.isAnnotationPresent(AfterSuite.class)) {
                a++;
            } else if (o.isAnnotationPresent(Test.class)){
                prioritySet.add((o.getAnnotation(Test.class)).priority());//добавляем в Set приоритеты аннотаций
            }
        }

        Iterator iterator1 = prioritySet.iterator();//складываем уники из сета в лист
        while(iterator1.hasNext()){
            mProirity.add((Integer) iterator1.next());
        }

        if ((b > 1) | (a > 1)) {
            throw new RuntimeException("Аннотации расставлены избыточно");
        } else {
            System.out.println("Аннотации указаны верно запускаем тест:");
            Collections.sort(mProirity);
            for (Method o : jMethods) { // сначала выводим метод с аннотацией BeforeSuite
                if (o.isAnnotationPresent(BeforeSuite.class)) {
                    o.invoke(null);
                }
            }
            for (int i = 0; i < mProirity.size(); i++) { //теперь прогоняем все методы помеченные аннотацией Test
                for (Method o : jMethods) {
                    if (!((o.getAnnotation(Test.class)) == null)) {
                        if (mProirity.get(i).equals((o.getAnnotation(Test.class)).priority())) {
                            o.invoke(null);
                        }
                    }
                }
            }

            for (Method o : jMethods) {
                if (o.isAnnotationPresent(AfterSuite.class)) {
                    o.invoke(null);
                }
            }

        }
    }


    //базовые примеры использования refflectionAPI
    public static void reflectionBasicCode() throws Exception {
        Class c1 = Cat.class;// 3 способа создания объекта типа Класс
        Class c2 = new Cat("Barsic", 10, "black").getClass();
        Class c3 = Class.forName("com.geekbrains.refflection.trash.Cat");


        Constructor constr = c1.getConstructor(String.class, int.class, String.class);//решили создать кота другим способом,
        // для этого создаем конструктор класса кэт
        Cat cat = (Cat) constr.newInstance("Bobik", 5, "White");//и создаем с его помощью кота
        cat.info();//проверяем удалось ли

        Method[] methods = c1.getMethods(); // при получении списка методов класса таким образом получаем только список открытых public методов
        for (Method o : methods) {
            System.out.println(o);
        }

        Method[] methodsAll = c1.getDeclaredMethods(); // получаем список всех методов даже закрытх модификаторами доступа
        for (Method o : methodsAll) {
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
