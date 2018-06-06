package net.eduard.api.test.autobase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Informacao da Coluna
 * @author Eduard
 * 
 * @version 1.0
 * @since Lib v2.0
 *
 */
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
