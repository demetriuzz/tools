package ru.demetriuzz.tools.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * При заполнении поля объекта из JSON представления<br/>
 * всегда выставлять СВОЕ значение UID (UniqueID (UUID)).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface JSONReplaceUID {}