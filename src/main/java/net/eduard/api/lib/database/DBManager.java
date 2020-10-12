package net.eduard.api.lib.database;

import java.sql.*;
import java.util.*;

import net.eduard.api.lib.database.api.SQLOption;
import net.eduard.api.lib.database.mysql.MySQLOption;
import net.eduard.api.lib.database.sqlite.SQLiteOption;
import net.eduard.api.lib.modules.Extra;

/**
 * API de Controle de MySQL ou SQLite com apenas 1 conexão
 *
 * @author Eduard
 */
@SuppressWarnings({"unchecked", "unused"})
public class DBManager {

    private static boolean debug = true;


    public static boolean isDebugging() {
        return debug;
    }

    public static void setDebug(boolean flag) {
        debug = flag;
    }

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
    private SQLEngineType engine = SQLEngineType.MYSQL;
    private SQLOption option;
    private transient Connection connection;


    public DBManager() {

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
     * Cria uma connexão com o Driver
     *
     * @return Conexão nova criada
     * @throws Exception Erro
     */
    public Connection connect() throws Exception {


        return DriverManager.getConnection(engine.getUrl(host, Extra.toInt(port), user, pass, database),user,pass);
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
                if (engine == SQLEngineType.MYSQL) {
                    option = new MySQLOption();
                    createDatabase(database);
                    useDatabase(database);

                } else if(engine == SQLEngineType.SQLITE) {
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

        String builder = option.createTable() +
                option.name(table) +
                option.fieldOpen() +
                " ID " + option.sqlTypeOf(Integer.class, 10) +
                option.notNull() +
                option.primaryKey() +
                option.autoIncrement() +
                option.fieldSeparator() +
                values +
                option.fieldClose();
        update(builder);
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
     * @return Id gerado pelo Insert
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
    public void deleteData(String table, int index) {

        update(option.deleteData() + table + " WHERE ID = ?", index);
    }

    /**
     * Deleta um registro
     *
     * @param table  Tablea
     * @param where  Como
     * @param values Valores
     */
    public void deleteData(String table, String where, Object... values) {
        update("DELETE FROM " + table + " WHERE " + where, values);
    }

    /**
     * Deleta uma coluna
     *
     * @param table  Tale
     * @param column Coluna
     */
    public void deleteColumn(String table, String column) {

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

    /**
     * Cria uma view com um select
     *
     * @param view   View
     * @param select Select query
     */
    public void createView(String view, String select) {

        update("CREATE OR REPLACE VIEW " + view + " AS " + select);
    }

    /**
     * Deleta a view
     *
     * @param view View
     */
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
     * Modifica a Coluna da Tabela
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
     * @param table Tabela
     * @param alter Alteração
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
     * @param joinTable Tabela de Junção
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
     * @param table Tabela
     */
    public void clearTable(String table) {
        update("TRUNCATE TABLE " + table);
    }

    /**
     * Verifica se contem algo na tabela
     *
     * @param table  Tabela
     * @param where  Verificação
     * @param values Valores da verificação
     * @return Se contem
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
     * Executa uma Atualização com um Query
     *
     * @param query   Query Pesquisa
     * @param objects Objetos
     * @return -1 se o não ocorreu update em nada, e retorna o numero do update ou insert caso tenha feito pelo menos 1
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

    public double getDouble(String table, String column, String where, Object... objects) {
        return getData(Double.class, table, column, where, objects);
    }

    public double getInt(String table, String column, String where, Object... objects) {
        return getData(Integer.class, table, column, where, objects);
    }


    public <T> T getData(Class<T> type, String table, String column, String where, Object... objects) {
        T result = null;
        if (hasConnection())
            try {
                ResultSet rs = selectAll(table, where, objects);
                if (rs.next()) {

                    result = (T) rs.getObject(column);
                }
                rs.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        return result;

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
                    Object data = option.convertToSQL(replacer, replacer.getClass(),null);

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
                    campo.setValueString(texto);
                    campo.setValue( valor);
                    campo.setTypeName( typeName);
                    campo.setType( type);
                    campo.setClassName( classe);
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


    @Override
    public String toString() {
        return "DBManager [user=" + user + ", pass=" + pass + ", host=" + host + ", port=" + port + ", database="
                + database + ", engine=" + engine + "]";
    }

    public boolean useSQLite() {

        return engine == SQLEngineType.SQLITE;
    }

    public void setUseSQLite(boolean useSQLite) {
        if (useSQLite)
            engine = SQLEngineType.SQLITE;

    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public SQLEngineType getEngine() {
        return engine;
    }
}
