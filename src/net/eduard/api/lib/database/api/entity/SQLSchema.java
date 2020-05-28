package net.eduard.api.lib.database.api.entity;

import java.util.ArrayList;
import java.util.List;

public class SQLSchema {
    private String name;
    private String charset;

    private List<String> tableNames = new ArrayList<>();
    private List<SQLTable> tables = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTableNames() {
        return tableNames;
    }

    public void setTableNames(List<String> tableNames) {
        this.tableNames = tableNames;
    }

    public List<SQLTable> getTables() {
        return tables;
    }

    public void setTables(List<SQLTable> tables) {
        this.tables = tables;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
