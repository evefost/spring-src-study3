package com.xie.java.annotation;

import com.xie.java.simple.BeanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Import(BeanRegistrar.class)
public @interface Tag {
    String value();
}
