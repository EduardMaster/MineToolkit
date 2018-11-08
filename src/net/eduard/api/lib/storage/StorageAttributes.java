package net.eduard.api.lib.storage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.FIELD, ElementType.TYPE, })
@Retention(RetentionPolicy.RUNTIME)
public @interface StorageAttributes {

	boolean reference() default false;

	boolean indentificate() default false;

	boolean inline() default false;
	
	//boolean auto() default true;

}