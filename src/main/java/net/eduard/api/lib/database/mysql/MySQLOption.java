package net.eduard.api.lib.database.mysql;

import net.eduard.api.lib.database.api.SQLOption;
import net.eduard.api.lib.modules.Extra;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;

public class MySQLOption implements SQLOption {
    @Override
    public String primaryKey() {
        return " PRIMARY KEY";
    }

    @Override
    public String autoIncrement() {
        return " AUTO_INCREMENT";
    }

    @Override
    public String sqlTypeOf(Class<?> javaClass, int size) {

        Class<?> wrapper = Extra.getWrapper(javaClass);
        if (wrapper != null) {
            javaClass = wrapper;
        }

        if (String.class.equals(javaClass)) {
            return "VARCHAR(" + size + ")";
        } else if (Integer.class.equals(javaClass)) {
            return "INTEGER";
        } else if (Double.class.equals(javaClass)) {
            return "DOUBLE";
        } else if (Byte.class.equals(javaClass)) {
            return "TINYINT";
        } else if (Short.class.equals(javaClass)) {
            return "SHORTINT";
        } else if (Long.class.equals(javaClass)) {
            return "BIGINT";
        } else if (Boolean.class.equals(javaClass)) {
            return "TINYINT(1)";
        } else if (Character.class.equals(javaClass)) {
            return "CHAR";
        } else if (Number.class.equals(javaClass)) {
            return "NUMERIC";
        } else if (Float.class.equals(javaClass)) {
            return "FLOAT";
        } else if (UUID.class.equals(javaClass)) {
            return "VARCHAR(40)";
        } else if (Timestamp.class.equals(javaClass)) {
            return "TIMESTAMP";
        } else if (Calendar.class.equals(javaClass)) {
            return "TIMESTAMP";
        } else if (Date.class.equals(javaClass)) {
            return "DATE";
        } else if (java.util.Date.class.equals(javaClass)) {
            return "DATE";
        } else if (Time.class.equals(javaClass)) {
            return "TIME";
        }
        return "VARCHAR(200)";

    }

}
