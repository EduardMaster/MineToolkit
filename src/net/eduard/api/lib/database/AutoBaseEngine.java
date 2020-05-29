package net.eduard.api.lib.database;

import net.eduard.api.lib.database.annotations.ColumnSize;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.*;

public class AutoBaseEngine {

    /*
    private static boolean DEBUG = true;
    private static boolean LOG = true;

    private static void debug(String query) {
        if (DEBUG)
            System.out.println("[AutoBaseEngine] Query: " + query);
    }

    private static void log(String msg) {
        if (LOG)
            System.out.println("[AutoBaseEngine] " + msg);
    }

    private Connection connection;


    public AutoBaseEngine(Connection conection) {
        this.connection = conection;
    }






    public void delete(Object data) {
        Class<?> type = data.getClass();

        try {
            int id = 1;
            id = AutoBaseUtil.getCacheId(data);
            String tableName = AutoBaseUtil.getTableName(type);
            log("DELETING ID " + id+ " FROM TABLE "+tableName);
            String query = "DELETE FROM `" + tableName + "` WHERE ID = " + id;
            try {
                debug(query);

                connection.prepareStatement(query).executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <E> void update(E data) {
        Class<?> type = data.getClass();
        try {
            String tableName = AutoBaseUtil.getTableName(type);
            int idCache = AutoBaseUtil.getCacheId(data);
            log("UPDATING ID " + idCache +  " FROM TABLE "+tableName);
            StringBuilder builder = new StringBuilder();
            builder.append("UPDATE `" + tableName + "` SET ");

            List<Object> list = new ArrayList<>();
            boolean first = true;
            for (Field field : type.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase("id")) {
                    continue;
                }
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (Modifier.isAbstract(field.getModifiers())) {
                    continue;
                }
                if (Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                String name = AutoBaseUtil.getFieldName(field);

                field.setAccessible(true);
                Object valor = field.get(data);
                if (field.isAnnotationPresent(ColumnSize.class)) {
                    ColumnSize options = field.getAnnotation(ColumnSize.class);
                    // Class<?> type = field.getType();
                    if (!options.nullable() && valor == null) {
                        continue;
                    }
                }
                if (!first) {
                    builder.append(", ");
                } else {
                    first = false;
                }
                builder.append("`" + name + "` = ? ");
                list.add(valor);

            }
            builder.append(" WHERE ID = " + idCache);

            debug(builder.toString());

            int id = DatabaseSQLUtil.update(connection, builder.toString(), list.toArray());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public int insertInto(Object data) {

        Class<?> type = data.getClass();
        try {
            String tableName =  AutoBaseUtil.getTableName(type);
            StringBuilder builder = new StringBuilder();
            log("INSERTING TO TABLE " + tableName);
            builder.append("INSERT INTO `" + tableName + "` ( ");
            boolean first = true;
            List<Object> list = new LinkedList<>();

            StringBuilder builder2 = new StringBuilder(" ) VALUES (");
            for (Field field : type.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase("id")) {
                    continue;
                }
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (Modifier.isAbstract(field.getModifiers())) {
                    continue;
                }
                if (Modifier.isTransient(field.getModifiers())) {
                    continue;
                }
                //log("Field: "+field.getName());
                if (!first) {
                    builder.append(", ");
                    builder2.append(", ");
                } else {
                    first = false;
                }
                builder.append("`" + AutoBaseUtil.getFieldName(field) + "`");
                field.setAccessible(true);
                Object valor = field.get(data);
                if (field.isAnnotationPresent(ColumnSize.class)) {
                    ColumnSize options = field.getAnnotation(ColumnSize.class);


                    if (!options.nullable() && valor == null) {
                        builder2.append(options.defValue());
                        continue;
                    }
                }
                builder2.append(" ? ");
                list.add(valor);


            }
            builder.append(builder2);
            builder.append(")");
            debug( builder.toString());


            int id = DatabaseSQLUtil.update(connection, builder.toString(), list.toArray());


            AutoBaseUtil.setCacheId(data, id);


            return id;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }





    public Connection getConection() {
        return connection;
    }

    public void setConection(Connection conection) {
        this.connection = conection;
    }
*/
}
