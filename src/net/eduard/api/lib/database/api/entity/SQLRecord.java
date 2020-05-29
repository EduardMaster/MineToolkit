package net.eduard.api.lib.database.api.entity;

import net.eduard.api.lib.database.api.SQLOption;

import javax.persistence.Column;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SQLRecord {

    private SQLTable table;
    private Map<SQLColumn, Object> data = new HashMap<>();
    private Object instance;

    public SQLRecord(SQLTable table, ResultSet resultSet, SQLOption option) {
        setTable(table);
        try {
            instance = (Object) table.getClassBased().newInstance();
            for (SQLColumn column : table.getColumns()) {
                try {
                    data.put(column, option.convertToJava(resultSet.getObject(column.getName())));

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception exception) {

            exception.printStackTrace();
        }
        save();
    }

    public SQLRecord(SQLTable table, Object instance) {
        setTable(table);
        setInstance(instance);
        reload();
    }

    public void save() {
        for (SQLColumn column : table.getColumns()) {

            try {
                column.getField().set(instance, data.get(column));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    public void reload() {
        data.clear();
        for (SQLColumn column : table.getColumns()) {

            try {
                Object value = column.getField().get(instance);
                data.put(column, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
    }

    public Object getInstance() {
        return instance;
    }

    public void setInstance(Object instance) {
        this.instance = instance;
    }


    public Map<SQLColumn, Object> getData() {
        return data;
    }

    public void setData(Map<SQLColumn, Object> data) {
        this.data = data;
    }

    public SQLTable getTable() {
        return table;
    }

    public void setTable(SQLTable table) {
        this.table = table;
    }
}
