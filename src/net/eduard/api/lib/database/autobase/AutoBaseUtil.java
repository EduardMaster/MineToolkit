package net.eduard.api.lib.database.autobase;

import net.eduard.api.lib.database.DatabaseSQLUtil;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class AutoBaseUtil {
    /**
     * Pega o tipo SQL de um tipo Java
     *
     * @param field Variavel java
     * @return Tipo SQL
     */
    public static String getSQLType(Field field) {
        StringBuilder builder = new StringBuilder();
        try {
            field.setAccessible(true);
            Class<?> typeClass = field.getType();

            boolean unique = false;
            boolean nullable = false;
            //create table a (nome teste default 'oi')
            String type = DatabaseSQLUtil.getSQLType(typeClass, 11);
            if (field.isAnnotationPresent(ColumnOption.class)) {
                ColumnOption options = field.getAnnotation(ColumnOption.class);
                nullable = options.nullable();
                unique = options.unique();
                type = DatabaseSQLUtil.getSQLType(typeClass, options.size());

                if (!options.type().isEmpty()) {
                    type = options.type();
                }
                if (!options.defValue().isEmpty()){
                    type += " DEFAULT "+ options.defValue();
                }

            }
//
            builder.append(type);

            if (!nullable) {
                builder.append(" NOT NULL");
            }

            if (unique) {
                builder.append(" UNIQUE");
            }

        } catch (Exception e) {
            e.printStackTrace();
            builder.append("VARCHAR(100) NOT NULL");
        }

        return builder.toString();
    }



    public static String getName(Class<?> clz) {
        try {
            return clz.getSimpleName();

        } catch (Error er) {
            return "Class" + clz.hashCode();
        }
    }

    public static String getFieldName(Field field) {
        if (field.isAnnotationPresent(ColumnName.class)) {
            return field.getAnnotation(ColumnName.class).value();
        }
        return field.getName();
    }

    public static String getTableName(Class<?> type) {
        if (type.isAnnotationPresent(TableName.class)) {
            return type.getAnnotation(TableName.class).value();
        }
        return getName(type);
    }

    public static int getCacheId(Object instance) {

        try {

            Field getId = instance.getClass().getDeclaredField("id");
            getId.setAccessible(true);
            int id = getId.getInt(instance);
            return id;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static void setCacheId(Object instance, int id) {

        try {

            Field getId = instance.getClass().getDeclaredField("id");
            getId.setAccessible(true);
            getId.set(instance, id);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
