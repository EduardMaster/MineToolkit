package net.eduard.api.lib.database.api.entity;

import javax.persistence.Column;
import java.util.HashMap;
import java.util.Map;

public class SQLRecord {

    private SQLTable table;
    private Map<SQLColumn, Object> data = new HashMap<>();

    public SQLRecord(SQLTable table,Object instance){
        setTable(table);
        reload(instance);
    }

    public void reload(Object instance){
        for (SQLColumn column : table.getColumns()){

            try {
                Object value  = column.getField().get(instance);
                data.put(column,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
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
