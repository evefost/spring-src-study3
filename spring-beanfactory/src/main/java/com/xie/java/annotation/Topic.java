package com.xie.java.annotation;

import com.xie.java.withannotation.BeanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(BeanRegistrar.class)
public @interface Topic {
    String value();
}
