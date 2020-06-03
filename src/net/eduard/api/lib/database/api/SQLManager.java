package net.eduard.api.lib.database.api;

import net.eduard.api.lib.database.api.entity.SQLColumn;
import net.eduard.api.lib.database.api.entity.SQLRecord;
import net.eduard.api.lib.database.api.entity.SQLTable;
import net.eduard.api.lib.database.mysql.MySQLQueryBuilder;
import net.eduard.api.lib.database.sqlite.SQLiteQueryBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLManager {

    private Connection connection;
    private SQLEngineType engineType;
    private SQLQueryBuilder builder;
    private Map<Class<?>, SQLTable> cacheTables = new HashMap<>();

    public SQLManager(Connection connection, SQLQueryBuilder queryBuilder) {
        setConnection(connection);
        setBuilder(builder);
        setEngineType(SQLEngineType.OTHER);
    }

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

    public <E> E getData(Class<E> dataClass, String fieldName, Object fieldValue) {

        SQLTable table = getTableData(dataClass);
        SQLColumn column = table.getColumn(fieldName);
        ResultSet result = executeQuery(builder.findRecord(table, column, fieldValue));
        E dataToReturn = null;
        if (result != null) {
            try {
                if (result.next()) {
                    SQLRecord record = new SQLRecord(table, result, builder.option());
                    table.getRecords().add(record);

                    dataToReturn = (E) record.getInstance();
                }

                result.getStatement().close();
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        }
        return dataToReturn;
    }


    public <E> E getData(Class<E> dataClass, Object primaryKeyValue) {
        SQLTable table = getTableData(dataClass);

        return getData(dataClass, table.getPrimaryKey().getField().getName(), primaryKeyValue);

    }

    public <E> List<E> getAllData(Class<E> dataClass) {
        List<E> list = new ArrayList<>();
        SQLTable table = getTableData(dataClass);

        ResultSet result = executeQuery(builder.findRecords(table));
        try {
            while (result.next()) {
                SQLRecord record = new SQLRecord(table, result, builder.option());
                list.add((E) record.getInstance());
            }

            result.getStatement().close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public void insertData(Object data) {
        Class<?> dataClass = data.getClass();
        SQLTable table = getTableData(dataClass);
        SQLRecord record = table.getRecord(data);
        int intReturned = executeUpdate(builder.insertRecord(record));
        if (intReturned != -1) {
            record.getData().put(table.getPrimaryKey(), intReturned);
            record.save();
        }
    }

    public void updateData(Object data) {
        Class<?> dataClass = data.getClass();
        SQLTable table = getTableData(dataClass);
        SQLRecord record = table.getRecord(data);
        record.reload();
        executeUpdate(builder.updateRecord(record));
    }

    public void deleteData(Object data) {
        Class<?> dataClass = data.getClass();
        SQLTable table = getTableData(dataClass);
        SQLRecord record = table.getRecord(data);
        executeUpdate(builder.deleteRecord(record));
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

    protected void log(String msg) {
        System.out.println("SQLManager: " + msg);
    }

    protected int executeUpdate(String query) {
        int id = -1;
        try {
            log("Update: " + query);
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();

            if (keys != null) {
                if (keys.next()) {
                    id = keys.getInt(1);
                }
            }
            keys.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    protected ResultSet executeQuery(String queryStr) {
        try {
            log("Query: " + queryStr);
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
