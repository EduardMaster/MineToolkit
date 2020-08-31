package net.eduard.api.lib.database.api;

import net.eduard.api.lib.database.annotations.TableName;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class SQLTable {
    private String tableName;
    private String charset;
    private Class<?> classBased;
    private final List<SQLColumn> columns = new ArrayList<>();
    private final List<SQLRecord> records = new ArrayList<>();

    public SQLTable(Class<?> dataClass) {
        setClassBased(dataClass);
        reload(dataClass);
        charset = "UTF8";
    }

    public SQLColumn getPrimaryKey() {
        for (SQLColumn column : columns) {
            if (column.isPrimary()) {
                return column;
            }
        }
        return null;
    }

    public SQLColumn getColumn(String fieldName) {
        for (SQLColumn column : columns) {
            if (column.getField().getName().equalsIgnoreCase(fieldName)) {
                return column;
            }
        }
        return null;
    }

    public void reload(Class<?> tableClass) {
        this.tableName = tableClass.getSimpleName();
        if (tableClass.isAnnotationPresent(TableName.class)) {
            this.tableName = tableClass.getAnnotation(TableName.class).value();
        }
        columns.clear();
        for (Field field : tableClass.getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            if (Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            columns.add(new SQLColumn(field));
        }
    }

    public SQLRecord getRecord(Object data) {
        for (SQLRecord record : records) {
            if (record.getInstance() == data) {
                return record;
            }
        }
        SQLRecord record = new SQLRecord(this, data);
        records.add(record);
        return record;
    }


    public String getTableName() {
        return tableName;
    }

    public List<SQLColumn> getColumns() {
        return columns;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Class<?> getClassBased() {
        return classBased;
    }

    public void setClassBased(Class<?> classBased) {
        this.classBased = classBased;
    }

    public List<SQLRecord> getRecords() {
        return records;
    }

}
