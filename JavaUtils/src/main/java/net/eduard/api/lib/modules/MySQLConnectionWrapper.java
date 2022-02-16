package net.eduard.api.lib.modules;


import java.sql.*;

public class MySQLConnectionWrapper {
    private String host;
    private String port;
    private String database;
    private String user;
    private String password;
    public MySQLConnectionWrapper(String host, String port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    // sqlite Link connection = "jdbc:sqlite:%database"
    public Connection openConnection() throws SQLException {
        final String connectionText = "jdbc:mysql://%host:%port/%database?username=%user&password=%password&autoReconnect=false"
                .replace("%host", host)
                .replace("%port", port)
                .replace("%database", database)
                .replace("%user", user)
                .replace("%password", password);
        return DriverManager.getConnection(
                connectionText, user, password);
    }
    public interface RequestLoopAction {
        void loopAction(ResultSet resultSet) throws SQLException;
    }

    public void request(String query, RequestLoopAction action, Object... objects) {
        Connection con = null;
        try {
            con = openConnection();
            PreparedStatement queryRequest = null;
            ResultSet queryResult = null;
            try {
                queryRequest = query(con, query, objects);
                queryResult = queryRequest.executeQuery();
                if (queryResult != null) {
                    while(queryResult.next()) {
                        action.loopAction(queryResult);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (queryResult != null)
                    queryResult.close();
                if (queryRequest != null)
                    queryRequest.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void createTable(String table, String collumns) {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        builder.append(table);
        builder.append(" ( ID INT AUTO_INCREMENT PRIMARY KEY,");
        builder.append(collumns);
        builder.append(")");
        update(builder.toString());
    }

    public int insert(String table, Object... objects) {
        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ").append(table).append(" VALUES ( DEFAULT , ");
        int index = 0;
        while (index < objects.length) {
            builder.append("? ,");
            index++;
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(")");
        return update(
                builder.toString(),
                objects
        );
    }


    public int update(String query, Object... objects) {
        int resultado = -1;
        Connection con = null;
        try {
            con = openConnection();
            PreparedStatement state = null;
            ResultSet keys = null;
            try {
                state = query(con, query, objects);
                resultado = state.executeUpdate();
                keys = state.getGeneratedKeys();
                if (keys != null) {
                    if (keys.next()) {
                        resultado = keys.getInt(1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (keys != null)
                    keys.close();
                if (state != null)
                    state.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return resultado;
    }


    public PreparedStatement query(Connection connection, String query, Object... objects
    ) throws SQLException {
        if (!query.endsWith(";")) {
            query += ";";
        }
        final PreparedStatement state = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        int id = 1;
        for (Object object : objects) {
            if (object == null) continue;
            state.setObject(id, object);
            id++;
        }
        return state;
    }
}
