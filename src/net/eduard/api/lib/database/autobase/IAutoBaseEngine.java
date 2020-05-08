package net.eduard.api.lib.database.autobase;

public interface IAutoBaseEngine {

    Object convertJavaToSQL(Object javaObject, Class<?> type);

    Object convertSQLToJava(Object sqlObject, Class<?> type);

    String getSQLTypeFromJava(Class<?> type, int size);

   // ResultSet query(String query, Object... array);

    //PreparedStatement prepare(String query, Object... array);

}
