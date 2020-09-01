package net.eduard.api.lib.database.api;

import net.eduard.api.lib.storage.StorageAPI;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

public interface SQLOption {

    String primaryKey();

    String autoIncrement();

    default String fieldOpen() {
        return " ( ";
    }

    default String fieldPlaceholder() {
        return " ? ";
    }

    default String fieldClose() {
        return " ) ";
    }

    default String fieldSeparator() {
        return " , ";
    }

    default String notNull() {
        return " NOT NULL";
    }

    default String useDefault() {
        return "DEFAULT";
    }


    default String defaults() {
        return " DEFAULT ";
    }

    default String name(String name) {
        return "`" + name + "`";
    }

    default String nullable() {
        return "NULL";
    }

    default String defaultCharset() {
        return "default charset = utf8";
    }

    default String values() {
        return " VALUES ";
    }

    default String equalsTo() {
        return " = ";
    }


    default String currentTimestamp() {
        return "CURRENT_TIMESTAMP()";
    }

    default String defaultFor(Class<?> javaClass) {
        if (Time.class.equals(javaClass)) {
            return data(new Time(System.currentTimeMillis()).toString());
        } else if (Timestamp.class.equals(javaClass) || Calendar.class.equals(javaClass)) {
            return currentTimestamp();
        } else if (java.util.Date.class.equals(javaClass) || Date.class.equals(javaClass)) {
            return data(new Date(System.currentTimeMillis()).toString());
        }
        return data("0");

    }

    default String allCollumns() {
        return "*";
    }

    default String createTable() {
        return "CREATE TABLE IF NOT EXISTS ";
    }

    default String deleteTable() {
        return "DROP TABLE IF EXISTS ";
    }


    default String clearTable() {
        return "TRUNCATE TABLE ";
    }


    default String insertData() {
        return "INSERT INTO ";
    }

    default String deleteData() {
        return "DELETE FROM ";
    }

    default String updateData() {
        return "UPDATE ";
    }

    default String updateDataSet() {
        return " SET ";
    }

    default String selectData() {
        return "SELECT " + allCollumns() + " FROM ";
    }

    default String where() {
        return " WHERE ";
    }

    default String unique() {
        return " UNIQUE";
    }

    default String orderBy(String orderCollumn, boolean decending) {
        return "ORDER BY " + name(orderCollumn) + " " + (decending ? "DESC" : "ASC");
    }

    default String data(String data) {
        return "'" + data + "'";
    }

    String sqlTypeOf(Class<?> javaClass, int size);


    default String convertToSQL(Object value, Class<?> javaClass, SQLColumn column) {

        if (value == null) {
            return null;
        }

        if (javaClass == Boolean.class) {
            value = ((boolean) value) ? 1 : 0;
        } else if (javaClass == java.util.Date.class) {
            value = new Date(((java.util.Date) value).getTime());
        } else if (value instanceof Calendar) {
            value = new Timestamp(((Calendar) value).getTimeInMillis());
        } else if (value instanceof Timestamp | value instanceof Time) {

        } else if (value instanceof UUID) {
            value = value.toString();
        } else {
            if (column != null) {
                if (column.isInline()) {
                    return StorageAPI.storeInline(javaClass, value);
                }
                if (column.isJson()) {
                    return StorageAPI.getGson().toJson(StorageAPI.storeField(column.getField(), value));
                }
            }
        }
        return value.toString();
    }

    default Object convertToJava(Object value, Class<?> javaClass, SQLColumn column) {
        if (javaClass == Calendar.class) {
            if (value instanceof Timestamp) {
                Timestamp timestamp = (Timestamp) value;
                Calendar calendario = Calendar.getInstance();
                calendario.setTimeInMillis(timestamp.getTime());
                return calendario;
            }
        } else if (javaClass == java.util.Date.class) {
            if (value instanceof Date) {
                Date date = (Date) value;
                return new java.util.Date(date.getTime());
            }
        } else if (javaClass == UUID.class) {
            return UUID.fromString(value.toString());
        } else {
            if (column.isInline()) {
                return StorageAPI.restoreInline(javaClass, value);
            }
            if (column.isJson()) {
                Map<?, ?> map = StorageAPI.getGson().fromJson(value.toString(), Map.class);
                return StorageAPI.restoreField(column.getField(), map);
            }

        }

        return value;
    }


}
