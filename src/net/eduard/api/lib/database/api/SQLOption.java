package net.eduard.api.lib.database.api;

public interface SQLOption {

    String primaryKey();

    String autoIncrement();

    String varText(int size);

    default String notNull() {
        return "NOT NULL";
    }

    default String nullable() {
        return "NULL";
    }

    default String currentTimestamp() {
        return "CURRENT_TIMESTAMP()";
    }

    default String allCollumns() {
        return "*";
    }

    default String createTable() {
        return "CREATE TABLE IF NOT EXISTS";
    }

    default String deleteTable() {
        return "DROP TABLE ";
    }


    default String clearTable() {
        return "TRUNCATE TABLE ";
    };

    default String insertData(){
        return "INSERT INTO ";
    }

    default String unique() {
        return "UNIQUE";
    }

    default String orderBy(String orderCollumn, boolean decending) {
        return "ORDER BY " + name(orderCollumn) + " " + (decending ? "DESC" : "ASC");
    }

    default String data(String data) {
        return "'" + data + "'";
    }
    default String convertToSQL(Object javaObject){

        return javaObject.toString();
    }
    default Object convertToJava(String sqlData){
        return sqlData;
    }

    default String name(String name) {
        return "`" + name + "`";
    }


    default String defaults(){
        return "DEFAULT ";
    }
}
