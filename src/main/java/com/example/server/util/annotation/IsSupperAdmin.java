package com.example.server.util.annotation;

import org.springframework.core.annotation.Order;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 全鸿润
 */
@Order(1)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsSupperAdmin {
}
