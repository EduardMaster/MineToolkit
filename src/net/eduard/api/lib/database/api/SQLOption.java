package net.eduard.api.lib.database.api;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAPI;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

public interface SQLOption {

    String primaryKey();

    String autoIncrement();

    default String notNull() {
        return " NOT NULL";
    }

    default String nullable() {
        return "NULL";
    }

    default String equalsTo() {
        return " = ";
    }

    default String currentTime() {
        return "CURRENT_TIME";
    }

    default String currentDate() {
        return "CURRENT_DATE";
    }

    default String currentTimestamp() {
        return "CURRENT_TIMESTAMP()";
    }

    default String defaultFor(Class<?> javaClass) {
        if (Time.class.equals(javaClass)) {
            return currentTime();
        } else if (Timestamp.class.equals(javaClass) || Calendar.class.equals(javaClass)) {
            return currentTimestamp();
        }else if (java.util.Date.class.equals(javaClass)|| Date.class.equals(javaClass)){
            return currentDate();
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
        return "DROP TABLE ";
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


    default String convertToSQL(Object value, Class<?> javaClass) {

        if (value == null) {
            return null;
        }


        if (javaClass == Boolean.class) {
            value = ((boolean) value) ? 1 : 0;
        }

        if (javaClass == java.util.Date.class) {
            value = new Date(((java.util.Date) value).getTime());
        } else if (value instanceof Calendar) {
            value = new Timestamp(((Calendar) value).getTimeInMillis());
        } else if (value instanceof UUID) {
            value = value.toString();
        }

        return value.toString();
    }

    default Object convertToJava(Object value, Class<?> javaClass) {


        if (javaClass == UUID.class) {
            return UUID.fromString(value.toString());
        }
        if (javaClass == Character.class) {
            return value.toString().toCharArray()[0];
        }
        if (javaClass == Calendar.class) {
            if (value instanceof Timestamp) {
                Timestamp timestamp = (Timestamp) value;
                Calendar calendario = Calendar.getInstance();
                calendario.setTimeInMillis(timestamp.getTime());
                return calendario;
            }
        }
        if (javaClass == java.util.Date.class) {
            if (value instanceof Date) {
                Date date = (Date) value;
                return new java.util.Date(date.getTime());
            }
        }

        return value;
    }

    default String name(String name) {
        return "`" + name + "`";
    }


    default String defaults() {
        return " DEFAULT ";
    }


}
