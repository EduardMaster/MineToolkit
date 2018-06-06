package net.eduard.api.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
	 
	 String name() default "";
	 
	 String type() default "";
	 
	 int size() default 255;
	 
	 boolean primary() default false;
	 
	 boolean nullable() default true;
	 
	 
	

}
