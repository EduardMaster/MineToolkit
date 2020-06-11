package net.eduard.api.lib.database;

import java.io.File;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import net.eduard.api.lib.database.api.SQLOption;
import net.eduard.api.lib.database.mysql.MySQLOption;
import net.eduard.api.lib.database.sqlite.SQLiteOption;

/**
 * API de Controle de MySQL ou SQLite com apenas 1 conexão
 *
 * @author Eduard-PC
 */
public class DBManager {

    private static boolean debug = true;

    private SQLOption option;

    public static void debug(String msg) {
        if (debug)
            System.out.println("[DB] " + msg);
    }

    private boolean enabled;
    private String user = "root";
    private String pass = "";
    private String host = "localhost";
    private String port = "3306";
    private String database = "mine";
    private String type = "jdbc:mysql://";

    private boolean useSQLite;
    private transient Connection connection;

    static {
        hasMySQL();
        hasSQLite();

    }

    public DBManager() {

    }

    /**
     * Ve se existe MySql na Maquina
     *
     * @return Se esta instalado MySQL na Maquina
     */
    public static boolean hasMySQL() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return true;
        } catch (Exception e) {
            // dont has mysql
            return false;
        }
    }

    public static boolean hasSQLite() {
        try {
            Class.forName("org.sqlite.JDBC");
            return true;

        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }
    }

    /**
     * Fecha a conexao do Banco
     */
    public void closeConnection() {
        if (hasConnection()) {
            try {
                this.connection.close();
            } catch (SQLException e) {

                e.printStackTrace();
            }
        }
    }

    /**
     * Cria uma connec§§o com a Database
     *
     * @return
     * @throws Exception
     */
    public Connection connectBase() throws Exception {
        return DriverManager.getConnection(getURL() + database + "?autoReconnect=true", user, pass);
    }

    /**
     * Cria uma conne§§o com o Driver
     *
     * @return
     * @throws Exception
     */
    public Connection connect() throws Exception {
        if (useSQLite) {
            String suffix = ".sqlite";
            File file = new File(database + suffix);
            file.getParentFile().mkdirs();
            file.createNewFile();
            return DriverManager.getConnection("jdbc:sqlite:" + database + suffix);
        } else {
            return DriverManager.getConnection(getURL() + "?autoReconnect=true", user, pass);
        }
    }

    /**
     * @return Texto da URL de acesso ao SQL Server
     */
    private String getURL() {
        return type + host + ":" + port + "/";
    }

    /**
     * Abre a conexao com o banco de dados caso nao exista ainda
     *
     * @return Mesma instacia da classe DBManager
     */
    public DBManager openConnection() {
        if (!hasConnection()) {
            try {
                this.connection = connect();
                if (!useSQLite) {
                    option = new MySQLOption();
                    createDatabase(database);
                    useDatabase(database);

                } else {
                    option = new SQLiteOption();
                }
            } catch (Exception e) {
                if (isDebugging()) {
                    e.printStackTrace();
                }
            }
        }

        return this;
    }

    /**
     * Ve se a conexao nao esta nula
     *
     * @return Se a conexao existe
     */
    public boolean hasConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Volta a conexao da variavel
     *
     * @return conexao atual
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Construtor pedindo Usuario, Senha, Host sem conectar com nenhum database
     * apenas no Driver
     *
     * @param user Usuario
     * @param pass Senha
     * @param host Host
     */
    public DBManager(String user, String pass, String host) {
        this(user, pass, host, "mine");
    }

    /**
     * Contrutor pedindo Usuario, Senha, Host, Database
     *
     * @param user     Usuario
     * @param pass     Senha
     * @param host     Host
     * @param database Database
     */
    public DBManager(String user, String pass, String host, String database) {
        this.user = user;
        this.pass = pass;
        this.host = host;
        this.database = database;
    }

    /**
     * Criar uma database
     *
     * @param database Database
     */
    public void createDatabase(String database) {
        update("create database if not exists " + database
                + " default character set utf8 default collate utf8_general_ci");
        // update("create database if not exists " + database + " default character set utf8");
    }

    /**
     * Conecta com a database
     *
     * @param database Database
     */
    public void useDatabase(String database) {
        update("USE " + database);
    }

    /**
     * Cria uma tabela
     *
     * @param table  Tabela
     * @param values Valores
     */
    public void createTable(String table, String values) {
        StringBuilder builder = new StringBuilder();
        builder.append(option.createTable());
        builder.append(option.name(table));
        builder.append(option.fieldOpen());
        builder.append(" ID " + option.sqlTypeOf(Integer.class, 10));
        builder.append(option.notNull());
        builder.append(option.primaryKey());
        builder.append(option.autoIncrement());
        builder.append(option.fieldSeparator());
        builder.append(values);
        builder.append(option.fieldClose());

        update(builder.toString());
    }

    /**
     * Deleta todas tabelas da database
     *
     * @param database Database
     */
    public void clearDatabase(String database) {
        update("TRUNCATE DATABASE " + database);
    }

    /**
     * Deleta database
     *
     * @param database Database
     */
    public void deleteDatabase(String database) {
        update("DROP DATABASE " + database);
    }

    /**
     * Insere um registro
     *
     * @param table   Tabela
     * @param objects Objetos
     * @return
     */
    public int insert(String table, Object... objects) {
        StringBuilder builder = new StringBuilder();
        builder.append(option.insertData());
        builder.append(option.name(table));
        builder.append(option.values());
        builder.append(option.fieldOpen());
        builder.append(option.useDefault());
        builder.append(option.fieldSeparator());
        for (int i = 0; i < objects.length; i++) {
            builder.append(option.fieldPlaceholder());
            builder.append(option.fieldSeparator());
        }
        builder.append(option.fieldOpen());

        return update(builder.toString(),
                objects);

    }

    /**
     * Deleta um registro
     *
     * @param table Tabela
     * @param index Index (ID)
     */
    public void delete(String table, int index) {
        update(option.deleteData() + table + " WHERE ID = ?", index);
    }

    /**
     * Deleta um registro
     *
     * @param table  Tablea
     * @param where  Como
     * @param values Valores
     */
    public void delete(String table, String where, Object... values) {
        update("DELETE FROM " + table + " WHERE " + where, values);
    }

    /**
     * Deleta uma coluna
     *
     * @param table  Tale
     * @param column Coluna
     */
    public void delete(String table, String column) {
        alter(table, "drop column " + column);
    }

    /**
     * Adiciona no Come§o da tabela uma coluna
     *
     * @param table          Tabela
     * @param columnComplete Coluna
     */
    public void addFirst(String table, String columnComplete) {
        alter(table, "add column " + columnComplete + " first");
    }

    public void addReference(String table, String key, String references) {
        update("ALTER TABLE " + table + " ADD FOREIGN KEY (" + key + ") REFERENCES " + references);
    }

    public void createView(String view, String select) {
        update("CREATE OR REPLACE VIEW " + view + " AS " + select);
    }

    public void deleteView(String view) {
        update("DROP VIEW " + view);
    }

    /**
     * Renomeia a Tabela para uma Nova Tabela
     *
     * @param table    Tabela
     * @param newTable Nova tabela
     */
    public void renameTable(String table, String newTable) {
        alter(table, "rename to " + newTable);
    }

    /**
     * Modifica uma Coluna de uma Tabela
     *
     * @param table        Tabela
     * @param column       Coluna
     * @param modification Modificar
     */
    public void modify(String table, String column, String modification) {
        alter(table, "modify column " + column + " " + modification);
    }

    /**
     * Adiciona chave primaria na tabela
     *
     * @param table Tabela
     * @param key   Chave
     */
    public void addKey(String table, String key) {
        alter(table, "add primary key (" + key + ")");
    }

    /**
     * Altera uma tabala
     *
     * @param table
     * @param alter
     */
    public void alter(String table, String alter) {
        if (hasConnection())
            update("alter table " + table + " " + alter);
    }

    /**
     * Modifica alguns registros da tabela
     *
     * @param table  Tabela
     * @param where  Como
     * @param edit   Modificar
     * @param values Valores
     */
    public void change(String table, String edit, String where, Object... values) {
        update("UPDATE " + table + " SET " + edit + " WHERE " + where, values);
    }

    /**
     * Cria um join entre as tabelas
     *
     * @param table     Tabela
     * @param joinTable Tabela2
     * @param onClause  Comparador
     * @param select    Select completo
     * @return ResultSet
     */
    public ResultSet join(String table, String joinTable, String onClause, String select) {
        return select(select + " FROM " + table + " JOIN " + joinTable + " ON " + onClause);
    }

    /**
     * Deleta a tabela
     *
     * @param table Tabela
     */
    public void deleteTable(String table) {
        update("DROP TABLE " + table);
    }

    /**
     * Limpa a tabela removendo todos registros
     *
     * @param table
     */
    public void clearTable(String table) {
        update("TRUNCATE TABLE " + table);
    }

    /**
     * @param table
     * @param where
     * @param values
     * @return
     */
    public boolean contains(String table, String where, Object... values) {
        return contains("select * from " + table + " where " + where, values);
    }

    /**
     * Executa um Select e volta se tem algum registro
     *
     * @param query   Query
     * @param objects Objetos
     * @return Se tem ou nao registro com esta Query
     */
    public boolean contains(String query, Object... objects
    ) {
        boolean has = false;
        if (hasConnection())
            try {
                ResultSet rs = select(query, objects
                );
                has = rs.next();
                rs.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        return has;

    }

    /**
     * Executa uma Atualiza§§o com um Query
     *
     * @param query   Query Pesquisa
     * @param objects Objetos
     * @return
     */
    public int update(String query, Object... objects
    ) {
        int resultado = -1;
        if (hasConnection()) {
            try {
                PreparedStatement state = query(query, objects
                );

                resultado = state.executeUpdate();
                ResultSet keys = state.getGeneratedKeys();
                if (keys != null) {
                    if (keys.next()) {
                        resultado = keys.getInt(1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return resultado;
    }

    /**
     * Cria um PreparedStatement com uma Query dada, e aplica os objects
     *
     * @param query   Query
     * @param objects Objetos
     * @return PreparedStatement (Estado da Query)
     */
    public PreparedStatement query(String query, Object... objects
    ) {
        try {
            if (!query.endsWith(";")) {
                query += ";";
            }

            PreparedStatement state = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            int id = 1;
            for (Object replacer : objects) {

                try {
                    Object data = option.convertToSQL(replacer, replacer.getClass());

                    state.setObject(id, data);

                } catch (SQLException e) {

                    e.printStackTrace();
                }
                id++;
            }


            debug("[MySQL] " + query);

            return state;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getString(String table, String column, String where, Object... objects
    ) {
        return this.getData(String.class, table, column, where, objects
        );
    }

    public Date getDate(String table, String column, String where, Object... objects
    ) {
        return this.getData(Date.class, table, column, where, objects
        );
    }

    public UUID getUUID(String table, String column, String where, Object... objects
    ) {
        return UUID.fromString(getString(table, column, where, objects
        ));
    }

    public int getInt(String table, String column, String where, Object... objects
    ) {
        return this.getData(Integer.class, table, column, where, objects
        );
    }

    public double getDouble(String table, String column, String where, Object... objects
    ) {
        return this.getData(Double.class, table, column, where, objects
        );
    }

    public <E> E getData(Class<E> type, String table, String column, String where, Object... objects
    ) {
        E result = null;
        ResultSet rs = selectAll(table, where, objects
        );
        try {
            if (rs.next()) {
                result = (E) rs.getObject(column, type);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public ResultSet selectAll(String table, String where, Object... objects
    ) {
        return select("SELECT * FROM " + table + " WHERE " + where, objects
        );
    }

    /**
     * Executa um Query e volta um ResultSet
     *
     * @param query   Pesquisa
     * @param objects Objetos
     * @return ResultSet (Resultado da Query)
     */
    public ResultSet select(String query, Object... objects
    ) {
        try {
            return query(query, objects
            ).executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "DBManager [user=" + user + ", pass=" + pass + ", host=" + host + ", port=" + port + ", database="
                + database + ", type=" + type + "]";
    }

    public boolean useSQLite() {
        return useSQLite;
    }

    public void setUseSQLite(boolean useSQLite) {
        this.useSQLite = useSQLite;
    }


    public static boolean isDebugging() {
        return debug;
    }

    public static void setDebug(boolean d) {
        debug = d;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    /**
     *
     */

    public List<Map<String, ColumnInfo>> getVariablesFrom(String tableName, String where) {
        List<Map<String, ColumnInfo>> lista = new LinkedList<>();

        if (!where.isEmpty()) {
            where = " WHERE " + where;
        }
        try {
            ResultSet rs = connection.prepareStatement("SELECT * FROM " + tableName + where).executeQuery();
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


    /**
     * Informações de uma Coluna de uma Tabela
     *
     * @author Eduard
     */
    public class ColumnInfo {
        private int id;
        private String name;
        private String text;
        private Object value;
        private int type;
        private String typeName;
        private String className;

        public Object get() {
            return getValue();
        }

        public String getString() {
            return text;
        }

        public Date getDate() {
            return (Date) getValue();
        }

        public int getInt() {
            return (int) getValue();
        }

        public double getDouble() {
            return (double) getValue();
        }

        public long getLong() {
            return (long) getValue();
        }

        public UUID getUUID() {
            return UUID.fromString(text);
        }

        public Class<?> getClassType() throws ClassNotFoundException {
            return Class.forName(className);
        }

        @Override
        public String toString() {
            return "" + value;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    }


}
