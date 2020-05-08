package net.eduard.api.lib.database;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAPI;

import java.sql.*;
import java.util.Calendar;
import java.util.UUID;

/**
 * Alguns métodos sobre controle de dados SQL
 */
public final class DatabaseSQLUtil {

    public static Object fromJavaToSQL(Object value) {
        if (value == null) {
            return null;
        }
        Class<? extends Object> type = value.getClass();
        Storable store = StorageAPI.getStore(type);
        if (store!=null){
            return store.store(value);
        }
        /*
        if (type == boolean.class || type == Boolean.class) {
            value = Boolean.valueOf(value.toString()) ? 1 : 0;
        }
        */
        if (type == java.util.Date.class) {
            value = new Date(((java.util.Date) value).getTime());
        } else if (value instanceof Calendar) {
            value = new Timestamp(((Calendar) value).getTimeInMillis());
        }else if( value instanceof UUID){
            value = value.toString();
        }

        return value;
    }
    public static Object fromSQLToJava(Class<?> type, Object value) {
        Storable store = StorageAPI.getStore(type);
        if (store!=null){
            return store.restore(value.toString());
        }
        if (type == UUID.class) {
            return UUID.fromString(value.toString());
        }
        if (type == Character.class) {
            return value.toString().toCharArray()[0];
        }
        if (type == Calendar.class) {
            if (value instanceof Timestamp) {
                Timestamp timestamp = (Timestamp) value;
                Calendar calendario = Calendar.getInstance();
                calendario.setTimeInMillis(timestamp.getTime());
                return calendario;
            }
        }
        if (type == java.util.Date.class) {
            if (value instanceof Date) {
                Date date = (Date) value;
                return new java.util.Date(date.getTime());
            }
        }

        return value;
    }

    public static PreparedStatement prepare(Connection connection, String query, Object... array) {
        PreparedStatement state = null;
        try {
            state = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int id = 1; id <= array.length; id++) {
                DatabaseSQLUtil.setSQLValue(state, array[id - 1], id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return state;

    }

    public static ResultSet query(Connection connection, String query, Object... array) {
        try {
            PreparedStatement state = DatabaseSQLUtil.prepare(connection, query, array);
            if (state != null) {
                return state.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getGeneratedId(PreparedStatement statement) {
        try {
            ResultSet keys = statement.getGeneratedKeys();
            if (keys != null) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public static int update(Connection connection, String query, Object... array) {
        try {
            PreparedStatement state = DatabaseSQLUtil.prepare(connection, query, array);
            if (state != null) {

                state.executeUpdate();
                int id = DatabaseSQLUtil.getGeneratedId(state);
                state.close();
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }


    public static String getSQLType(Class<?> type, int size) {
        Class<?> wrapper = Extra.getWrapper(type);
        if (wrapper != null) {
            type = wrapper;
        }
        Storable store = StorageAPI.getStore(type);
        if (store!=null){
            return "TEXT";
        }
        if (String.class.isAssignableFrom(type)) {
            return "VARCHAR" + "(" + size + ")";
        } else if (Integer.class == type) {
            return "INTEGER" + "(" + size + ")";
        } else if (Boolean.class == type) {
            return "TINYINT(1)";
        } else if (Short.class == type) {
            return "SMALLINT" + "(" + size + ")";
        } else if (Byte.class == type) {
            return "TINYINT" + "(" + size + ")";
        } else if (Long.class == type) {
            return "BIGINT" + "(" + size + ")";
        } else if (Character.class == type) {
            return "CHAR" + "(" + size + ")";
        } else if (Float.class == type) {
            return "FLOAT";
        } else if (Double.class == type) {
            return "DOUBLE";
        } else if (Number.class.isAssignableFrom(type)) {
            return "NUMERIC";
        } else if (Timestamp.class.equals(type)) {
            return "TIMESTAMP";
        } else if (Calendar.class.equals(type)) {
            return "DATETIME";
        } else if (Date.class.equals(type)) {
            return "DATE";
        } else if (java.util.Date.class.equals(type)) {
            return "DATE";
        } else if (Time.class.equals(type)) {
            return "TIME";
        } else if (UUID.class.isAssignableFrom(type)) {
            return "VARCHAR(40)";
        }

        return null;
    }

    public static void setSQLValue(PreparedStatement st, Object value, int column) {
        try {
            st.setObject(column, fromJavaToSQL(value));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Object getSQLValue(ResultSet rs, Class<?> type, String column) {
        Object result = null;
        try {
            Class<?> wrap = Extra.getWrapper(type);
            if (wrap != null) {
                type = wrap;
            }
            result = rs.getObject(column);
            if (type == Boolean.class) {
                result = rs.getBoolean(column);
            }
            if (type == Byte.class) {
                result = rs.getByte(column);
            }
            if (type == Short.class) {
                result = rs.getShort(column);
            }

            result = fromSQLToJava(type, result);
        } catch (SQLException e) {

            e.printStackTrace();
        }

        return result;
    }


    /**
     * Gera um texto com "?" baseado na quantidade<br>
     * Exemplo 5 = "? , ?,?,?,?"
     *
     * @param size Quantidade
     * @return Texto criado
     */
    public static String getQuestionMarks(int size) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (i != 0)
                builder.append(",");
            builder.append("?");
        }
        return builder.toString();
    }


}
