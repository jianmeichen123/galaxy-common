package com.galaxyinternet.framework.core.utils.MongoDB;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryField {
    QueryType type() default QueryType.EQUALS;
    String attribute() default "";
}