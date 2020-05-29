package net.eduard.api.lib.database.api;

import net.eduard.api.lib.database.api.entity.SQLTable;
import net.eduard.api.lib.database.mysql.MySQLQueryBuilder;
import net.eduard.api.lib.database.sqlite.SQLiteQueryBuilder;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SQLManager {

    private Connection connection;
    private SQLEngineType engineType;
    private SQLQueryBuilder builder;
    private Map<Class<?>, SQLTable> cacheTables = new HashMap<>();


    public SQLManager(Connection connection, SQLEngineType type) {
        setConnection(connection);
        setEngineType(type);
        switch (engineType) {
            case MYSQL:
                setBuilder(new MySQLQueryBuilder());
                break;
            case SQLITE:
                setBuilder(new SQLiteQueryBuilder());
                break;
            default:
                break;
        }
    }


    protected SQLTable getTableData(Class<?> dataClass) {
        SQLTable tableData = cacheTables.get(dataClass);
        if (tableData == null) {
            tableData = new SQLTable(dataClass);
            cacheTables.put(dataClass, tableData);
        }
        return tableData;
    }

    public void createTable(Class<?> dataClass) {
        SQLTable table = getTableData(dataClass);
        executeUpdate(builder.createTable(table));
    }

    public void deleteTable(Class<?> dataClass) {
        SQLTable table = getTableData(dataClass);
        executeUpdate(builder.deleteTable(table));
    }

    public void clearTable(Class<?> dataClass) {
        SQLTable table = getTableData(dataClass);
        executeUpdate(builder.clearTable(table));
    }

    protected  void log(String msg){
        System.out.println("SQLManager: "+msg);
    }

    protected void executeUpdate(String query) {
        try {
            log("Update: "+query);
            connection.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected ResultSet executeQuery(String queryStr) {
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
