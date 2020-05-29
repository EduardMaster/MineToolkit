package net.eduard.api.lib.database.api.entity;

import net.eduard.api.lib.database.annotations.*;
import net.eduard.api.lib.modules.Extra;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class SQLColumn {

    private String name;
    private Field field;
    private String sqlType;
    private Class<?> javaType;
    private Type javaGenericType;
    private Object defaultValue;
    private boolean isPrimary;
    private boolean isNullable;
    private boolean isUnique;
    private int size;

    public SQLColumn(Field field) {
        setField(field);
        getField().setAccessible(true);
        reload();
    }

    public void reload() {

        name = field.getName();
        if (field.isAnnotationPresent(ColumnName.class)) {
            name = field.getAnnotation(ColumnName.class).value();
        }
        javaType = field.getType();
        javaType = Extra.getWrapperOrReturn(javaType);
        javaGenericType = field.getGenericType();
        if (javaType.equals(String.class)) {
            size = 50;
        }

        if (field.isAnnotationPresent(ColumnValue.class)) {
            defaultValue = field.getAnnotation(ColumnValue.class).value();
        }
        if (field.isAnnotationPresent(ColumnType.class)) {
            sqlType = field.getAnnotation(ColumnType.class).value();
        }
        if (field.isAnnotationPresent(ColumnSize.class)) {
            size = field.getAnnotation(ColumnSize.class).value();
        }
        isUnique = field.isAnnotationPresent(ColumnUnique.class);
        isPrimary = field.isAnnotationPresent(ColumnPrimary.class);
        isNullable = field.isAnnotationPresent(ColumnNullable.class);
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSqlType() {
        return sqlType;
    }

    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
    }

    public Class<?> getJavaType() {
        return javaType;
    }

    public void setJavaType(Class<?> javaType) {
        this.javaType = javaType;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean nullable) {
        isNullable = nullable;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    public Type getJavaGenericType() {
        return javaGenericType;
    }

    public void setJavaGenericType(Type javaGenericType) {
        this.javaGenericType = javaGenericType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }
}
