package com.geekbrains.refflection;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//аннотация запускается вмсете с программой
@Target(ElementType.METHOD)//аннотация доступна к методам
public @interface Test {
    //String description() default "Без описания"; //добавляем к аннотации параметр и добавляем дефолтное значение
    int priority() default 5;
}
