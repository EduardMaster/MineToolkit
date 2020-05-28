package net.eduard.api.lib.database.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SQLManager {

    private Connection connection;
    private SQLEngineType  engineType;
    private SQLQueryBuilder builder;
    private Map<Class<?> , TableData> cacheTables = new HashMap<>();

    protected TableData getTableData(Class<?> dataClass){
        TableData tableData = cacheTables.get(dataClass);
        if (tableData==null){
            tableData = new TableData(dataClass);
            cacheTables.put(dataClass,tableData);
        }
        return tableData;
    }

    public SQLManager(Connection connection){
        setConnection(connection);
    }
    public void createTable(Class<?> dataClass){
        TableData table = getTableData(dataClass)
        executeUpdate(builder.createTable(table));
    }
    protected void executeUpdate(String query){
        try {
            connection.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    protected ResultSet executeQuery(String queryStr){
        try {
            ResultSet resultSet = connection.prepareStatement(queryStr).executeQuery();
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public SQLEngineType getEngineType() {
        return engineType;
    }

    public void setEngineType(SQLEngineType engineType) {
        this.engineType = engineType;
    }

    public SQLQueryBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(SQLQueryBuilder builder) {
        this.builder = builder;
    }
}
