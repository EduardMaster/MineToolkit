package net.eduard.api.lib.database.autobase;

import net.eduard.api.lib.database.DatabaseSQLUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.*;

public class AutoBaseEngine {
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


    public <E> E fetchById(int id, Class<E> type) {

        try {
            E instance = type.newInstance();
            AutoBaseUtil.setCacheId(instance, id);
            boolean returned = updateCache(instance);
            if (returned) {
                return instance;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    /**
     * Puxa os dados para memória
     *
     * @return Se puxou ou não os dados
     */
    public <E> boolean updateCache(E cache) {
        boolean hasUpdateCode = false;
        Class<?> type = cache.getClass();
        int id = 1;

        try {
            id = AutoBaseUtil.getCacheId(cache);

            StringBuilder builder = new StringBuilder();
            builder.append(
                    "SELECT * FROM `" + AutoBaseUtil.getTableName(type) + "` WHERE ID = " + id);
            log("FINDING BY ID " + id);
            debug(builder.toString());
            ResultSet rs = connection.prepareStatement(builder.toString()).executeQuery();

            if (rs.next()) {

                hasUpdateCode = true;
                for (Field field : type.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    if (Modifier.isAbstract(field.getModifiers())) {
                        continue;
                    }
                    if (Modifier.isTransient(field.getModifiers())) {
                        continue;
                    }
                    field.setAccessible(true);
                    Object value = DatabaseSQLUtil.getSQLValue(rs, field.getType(), AutoBaseUtil.getFieldName(field));
                    field.set(cache, value);
                }
            }
            rs.getStatement().close();
//			st.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasUpdateCode;
    }

    /**
     * SELECT * FROM 'TABLE'
     */
    public <E> List<E> fetchAll(Class<E> type) {
        List<E> list = new ArrayList<>();
        try {
            String tableName = AutoBaseUtil.getTableName(type);
            StringBuilder builder = new StringBuilder();
            builder.append("SELECT * FROM `" + tableName + "`");

            log("GETTING ALL DATA FROM " + tableName);
            debug(builder.toString());

            ResultSet rs = connection.prepareStatement(builder.toString()).executeQuery();
            while (rs.next()) {
                E newE = null;
                try {

                    newE = type.newInstance();

                } catch (Exception ex) {
                    continue;
                }

                for (Field field : newE.getClass().getDeclaredFields()) {

                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    if (Modifier.isAbstract(field.getModifiers())) {
                        continue;
                    }
                    if (Modifier.isTransient(field.getModifiers())) {
                        continue;
                    }
                    field.setAccessible(true);
                    Object value = DatabaseSQLUtil.getSQLValue(rs, field.getType(), AutoBaseUtil.getFieldName(field));
                    field.set(newE, value);
                }
                list.add(newE);
            }
            rs.getStatement().close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return list;
    }

    /**
     * CREATE TABLE IF NOT EXISTS 'TABLE'
     */
    public <E> void createTable(Class<E> type) {
        String tableName = AutoBaseUtil.getTableName(type);

        StringBuilder builder = new StringBuilder();

        builder.append("CREATE TABLE IF NOT EXISTS `" + tableName + "` (");
        log("CREATING TABLE " + tableName);

        boolean first = true;
        for (Field field : type.getDeclaredFields()) {
            if (field.getName().equalsIgnoreCase("id")) {
                builder.append("ID INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY , ");
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
            if (!first) {
                builder.append(", ");
            } else {
                first = false;
            }
            builder.append(" `" + AutoBaseUtil.getFieldName(field) + "`");
            builder.append(" " + AutoBaseUtil.getSQLType(field));

        }
        builder.append(")");
        builder.append(" default charset = UTF8");

        builder.append(";");
        try {
            connection.prepareStatement(builder.toString()).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    /**
     * DROP TABLE 'TABLE'
     */
    public <E> void deleteTable(Class<E> type) {

        try {
            String tableName = AutoBaseUtil.getTableName(type);
            log("DELETING TABLE " + tableName);
            connection.prepareStatement("DROP TABLE IF EXISTS `" + AutoBaseUtil.getTableName(type) + "`").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * TRUNCATE TABLE 'TABLE'
     */
    public void clearTable(Class<?> type) {
        try {
            String tableName = AutoBaseUtil.getTableName(type);
            log("CLEARING TABLE " + tableName);
            String query = "TRUNCATE TABLE `" + AutoBaseUtil.getTableName(type) + "`";
            debug(query);
            connection.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                if (field.isAnnotationPresent(ColumnOption.class)) {
                    ColumnOption options = field.getAnnotation(ColumnOption.class);
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
                if (field.isAnnotationPresent(ColumnOption.class)) {
                    ColumnOption options = field.getAnnotation(ColumnOption.class);


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


    public List<Map<String, ColumnInfo>> getVariablesFrom(Class<?> clz, String where) {
        List<Map<String, ColumnInfo>> lista = new LinkedList<>();

        if (!where.isEmpty()) {
            where = " WHERE " + where;
        }
        try {
            ResultSet rs = connection.prepareStatement("SELECT * FROM " + AutoBaseUtil.getName(clz) + where).executeQuery();
            ResultSetMetaData meta = rs.getMetaData();

            while (rs.next()) {
                Map<String, ColumnInfo> mapa = new LinkedHashMap<>();
                lista.add(mapa);
                for (int colunaID = 1; colunaID <= meta.getColumnCount(); colunaID++) {
                    String coluna = meta.getColumnName(colunaID);
                    String classe = meta.getColumnClassName(colunaID);
                    int type = meta.getColumnType(colunaID);
                    String typeName = meta.getColumnTypeName(colunaID);
                    Object valor = rs.getObject(colunaID);
                    String texto = rs.getString(colunaID);
                    // String calalog = meta.getCatalogName(colunaID);
                    // String label = meta.getColumnLabel(colunaID);
                    // int displaySize = meta.getColumnDisplaySize(colunaID);
                    // int precision = meta.getPrecision(colunaID);
                    // int scale = meta.getScale(colunaID);
                    ColumnInfo campo = new ColumnInfo();
                    campo.setText(texto);
                    campo.setValue(valor);
                    campo.setTypeName(typeName);
                    campo.setType(type);
                    campo.setClassName(classe);
                    campo.setName(coluna);
                    campo.setId(colunaID);
                    mapa.put(coluna, campo);
                }
            }
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;

    }


    public Connection getConection() {
        return connection;
    }

    public void setConection(Connection conection) {
        this.connection = conection;
    }

}
