package net.eduard.api.lib.database;

import net.eduard.api.lib.database.api.SQLColumn;
import net.eduard.api.lib.database.api.SQLQueryBuilder;
import net.eduard.api.lib.database.api.SQLRecord;
import net.eduard.api.lib.database.api.SQLTable;
import net.eduard.api.lib.database.mysql.MySQLQueryBuilder;
import net.eduard.api.lib.database.sqlite.SQLiteQueryBuilder;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SQLManager {

    private DBManager dbManager;
    private SQLQueryBuilder builder;
    private final Queue<Object> updatesQueue = new ConcurrentLinkedQueue<>();
    private final Map<Class<?>, SQLTable> cacheTables = new HashMap<>();

    public void setDbManager(DBManager dbManager) {
        this.dbManager = dbManager;
        switch (dbManager.getEngine()) {
            case MYSQL:
                builder = new MySQLQueryBuilder();
                break;
            case SQLITE:
            default:
                builder = new SQLiteQueryBuilder();
                break;

        }
    }

    public SQLManager() {

    }

    public SQLManager(DBManager dbManager) {
        setDbManager(dbManager);
    }

    public int runUpdatesQueue() {
        int amount = 0;
        int queueRunsLimit = 100;
        for (int i = 0; i < queueRunsLimit; i++) {
            Object data = updatesQueue.poll();

            if (data == null) break;
            updateData(data);
            amount++;
        }
        return amount;
    }


    public boolean hasConnection() {
        if (dbManager != null)
            return dbManager.hasConnection();
        return false;
    }

    @SuppressWarnings("unchecked")
    public <E> E getData(Class<E> dataClass, String fieldName, Object fieldValue) {

        SQLTable table = getTableData(dataClass);
        SQLColumn column = table.getColumn(fieldName);
        E dataToReturn = null;
        if (hasConnection()) {
            ResultSet result = executeQuery(builder.findRecord(table, column, fieldValue));

            if (result != null) {
                try {
                    if (result.next()) {
                        SQLRecord record = new SQLRecord(table, result, builder.option());
                        table.getRecords().add(record);

                        dataToReturn = (E) record.getInstance();
                    }

                    result.getStatement().close();
                } catch (Exception ignored) {
                }
            }
        }
        return dataToReturn;
    }


    public <E> E getData(Class<E> dataClass, Object primaryKeyValue) {
        SQLTable table = getTableData(dataClass);

        return getData(dataClass, table.getPrimaryKey().getField().getName(), primaryKeyValue);

    }

    public <E> List<E> getAllData(Class<E> dataClass) {
        return getAllData(dataClass, "", "", false, 0);
    }

    @SuppressWarnings("unchecked")
    public <E> List<E> getAllData(Class<E> dataClass, String where, String orderBy, boolean desc, int limit) {
        List<E> list = new ArrayList<>();
        SQLTable table = getTableData(dataClass);
        if (orderBy.isEmpty())
            orderBy = table.getPrimaryKey().getName();

        if (hasConnection()) {
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
        }

        return list;
    }

    public void insertData(Object data) {
        if (hasConnection()) {
            Class<?> dataClass = data.getClass();
            SQLTable table = getTableData(dataClass);
            SQLRecord record = table.getRecord(data);
            int intReturned = executeUpdate(builder.insertRecord(record));
            if (intReturned != -1) {
                record.getData().put(table.getPrimaryKey(), intReturned);
                record.save();
            }
        }
    }

    public void updateDataQueue(Object data) {
        if (updatesQueue.contains(data)) {
            return;
        }
        updatesQueue.add(data);

    }

    public void updateData(Object data) {
        if (hasConnection()) {
            Class<?> dataClass = data.getClass();
            SQLTable table = getTableData(dataClass);
            SQLRecord record = table.getRecord(data);
            record.reload();
            executeUpdate(builder.updateRecord(record));
        }
    }

    public void deleteData(Object data) {
        if (hasConnection()) {
            Class<?> dataClass = data.getClass();
            SQLTable table = getTableData(dataClass);
            SQLRecord record = table.getRecord(data);
            executeUpdate(builder.deleteRecord(record));
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
        if (hasConnection()) {
            executeUpdate(builder.createTable(table));
        }
    }

    public void deleteTable(Class<?> dataClass) {
        if (hasConnection()) {
            SQLTable table = getTableData(dataClass);
            executeUpdate(builder.deleteTable(table));
        }
    }

    public void clearTable(Class<?> dataClass) {
        if (hasConnection()) {
            SQLTable table = getTableData(dataClass);
            executeUpdate(builder.clearTable(table));
        }
    }

    protected void log(String msg) {
        System.out.println("SQLManager: " + msg);
    }

    protected int executeUpdate(String query) {
        int id = -1;
        try {
            log("Update: " + query);
            PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();

            if (keys != null) {
                if (keys.next()) {
                    id = keys.getInt(1);
                }
                keys.close();
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    protected ResultSet executeQuery(String queryStr) {
        try {
            log("Query: " + queryStr);
            return getConnection().prepareStatement(queryStr).executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Connection getConnection() {
        return dbManager.getConnection();
    }

    public SQLQueryBuilder getBuilder() {
        return builder;
    }

}
