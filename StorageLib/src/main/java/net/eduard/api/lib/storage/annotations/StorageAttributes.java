package net.eduard.api.lib.storage.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface StorageAttributes {

    boolean reference() default false;

    boolean indentificate() default false;

    boolean inline() default false;

    boolean referenceMapValue() default false;

}