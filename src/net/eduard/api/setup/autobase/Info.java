package net.eduard.api.setup.autobase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Info {

	boolean primaryKey() default false;

	String name() default "";

	boolean unique() default false;

	boolean update() default false;

	boolean canBeNull() default false;

	int size() default 100;

}
