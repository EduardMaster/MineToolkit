package net.eduard.api.lib.database.autobase;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Opções da Coluna<br>
 *
 *
 * @author Eduard
 *
 *
 *
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnOption {

    int size() default 11;

    boolean unique() default false;

    boolean nullable() default false;

    String type() default "";

    String defValue() default  "";
}
