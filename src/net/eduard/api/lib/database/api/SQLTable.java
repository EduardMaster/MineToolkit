package net.eduard.api.lib.database.api;

import net.eduard.api.lib.database.annotations.TableName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SQLTable {
    public SQLTable(Class<?> dataClass) {
        setClassBased(dataClass);
        reload(dataClass);
        charset="UTF8";


    }

    public void reload(Class<?> tableClass) {
        this.tableName = tableName;
        if (tableClass.isAnnotationPresent(TableName.class)){
            this.tableName = tableClass.getAnnotation(TableName.class).value();
        }
        columns.clear();
        for (Field field : tableClass.getDeclaredFields()) {
            columns.add(new SQLColumn(field));
        }
    }

    private String tableName;
    private String charset;
    private Class<?> classBased;
    private List<SQLColumn> columns = new ArrayList<>();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<SQLColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<SQLColumn> columns) {
        this.columns = columns;
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
}
