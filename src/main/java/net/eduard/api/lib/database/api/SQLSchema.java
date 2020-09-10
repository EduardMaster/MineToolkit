package net.eduard.api.lib.database.api;

import java.util.ArrayList;
import java.util.List;

public class SQLSchema {
    private String name;
    private String charset;

    private final List<String> tableNames = new ArrayList<>();
    private final List<SQLTable> tables = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTableNames() {
        return tableNames;
    }
    public List<SQLTable> getTables() {
        return tables;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
