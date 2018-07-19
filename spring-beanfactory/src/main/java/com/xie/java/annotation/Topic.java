package com.xie.java.annotation;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface Topic {

    @AliasFor("value")
    String topic() default "";

    @AliasFor("topic")
    String value() default "";





}
