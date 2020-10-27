package net.eduard.api.lib.database.annotations;

import net.eduard.api.lib.database.api.TableRelation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnRelation {
    TableRelation value() default TableRelation.ONE_TO_ANY;
}
