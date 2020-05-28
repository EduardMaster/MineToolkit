package net.eduard.api.lib.database.api;

import java.util.ArrayList;
import java.util.List;

public class TableData {
    public TableData(Class<?> claz){

    }
    private String tableName;
    private List<ColumnData> columns = new ArrayList<>();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnData> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnData> columns) {
        this.columns = columns;
    }


}
