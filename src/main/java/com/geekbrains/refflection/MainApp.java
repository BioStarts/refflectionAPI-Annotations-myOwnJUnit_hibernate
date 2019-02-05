package com.geekbrains.refflection;

/*
CREATE TABLE students (
    id    INTEGER PRIMARY KEY AUTOINCREMENT,
    name  TEXT,
    score INTEGER
);
*/

import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MainApp {
    private static Connection connection;
    private static Statement stmt;


    public static void main(String[] args) {
        try {
            connect();
            //generateTable(Teachers.class);
            addEntry(new Teachers(1,"Bob", "phD"));
            addEntry(new Student(1,"Jhon",4));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
    }

    public static void connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:mydb.db");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException e) {
           new SQLException("Ошибка при подключении к БД");
        }
    }

    public static void disconnect() {
        try {
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public static void generateTable(Class c){ //метод создающий таблицу из аннотрированного класса
        if (!c.isAnnotationPresent(Table.class)){
            throw new RuntimeException("Невозможно сгенерировать таблицу для класса без аннотации @Table");
        }
        Map<Class, String> converterMap = new HashMap<Class, String>();//создаем коллекцию соответствий типов данных Java с типами таблицы SQLite
        converterMap.put(int.class, "INTEGER");
        converterMap.put(String.class, "TEXT");

        StringBuilder sb = new StringBuilder("CREATE TABLE ");
        sb.append(((Table)c.getAnnotation(Table.class)).title());//выцепили и добавили название таблицы из класса
        sb.append(" (");
        Field[] fields = c.getDeclaredFields();
        for (Field o:fields) {
            if (o.isAnnotationPresent(Column.class)){
                sb.append(o.getName()).append(" ").append(converterMap.get(o.getType())).append(", ");// собираем строку из названий и типов полей, которые присутсвуют в классе из которого лепим таблицу
            }
        }
        sb.setLength(sb.length() - 2);//отрезаем лишнюю запятую и пробел
        sb.append(");");
        String query = sb.toString();
        try {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addEntry (Object obj){
        Class c = obj.getClass();
        if (!c.isAnnotationPresent(Table.class)){
            throw new RuntimeException("Класс без аннотации @Table");
        }

        StringBuilder sb = new StringBuilder("INSERT INTO ");
        sb.append(((Table)c.getAnnotation(Table.class)).title());//выцепили и добавили название таблицы из класса
        sb.append(" (");
        Field[] fields = c.getDeclaredFields();
        for (Field o:fields) {
            if (o.isAnnotationPresent(Column.class)){
                sb.append(o.getName()).append(", ");// собираем строку из типов полей, которые присутсвуют в классе из которого лепим запрос на создание элемента
            }
        }
        sb.setLength(sb.length() - 2);//отрезаем лишнюю запятую и пробел
        sb.append(") VALUES (");

        for (Field o:fields) {
            if (o.isAnnotationPresent(Column.class)){
                sb.append("?, ");// собираем строку для последующего prepareStatement
            }
        }
        sb.setLength(sb.length() - 2);//отрезаем лишнюю запятую и пробел
        sb.append(");");


        String query = sb.toString();
        try {
            PreparedStatement psInsert = connection.prepareStatement(query);
            int counter = 1;
            for (Field o:fields) {
                if (o.isAnnotationPresent(Column.class)){
                    psInsert.setObject(counter, o.get(obj));
                    counter++;
                }
            }
            psInsert.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
