package net.eduard.api.setup.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.eduard.api.setup.StorageAPI.Copyable;
import net.eduard.api.setup.StorageAPI.Storable;

/**
 * API de Controle de MySQL ou SQLite com apenas 1 conexão
 * 
 * @author Eduard-PC
 *
 */
public class DBManager implements Storable, Copyable {

	private static boolean debug = true;
	private String user = "root";
	private String pass = "";
	private String host = "localhost";
	private String port = "3306";
	private String database = "teste";
	private String type = "jdbc:mysql://";
	private boolean useSQLite;
	private transient Connection connection;
	private transient ResultSet result;
	private transient PreparedStatement statement;

	@Override
	public DBManager copy() {
		return copy(this);
	}

	static {
		hasMySQL();
		hasSQLlite();
	}

	/**
	 * Fecha tudo
	 */
	public void closeAll() {
		closeState();
		closeResult();
		closeConnection();
	}

	/**
	 * Fecha o Select feito
	 */
	public void closeSelect() {
		closeState();
		closeResult();
	}

	public DBManager() {
		// TODO Auto-generated constructor stub
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

	public static boolean hasSQLlite() {
		try {
			Class.forName("org.sqlite.JDBC");
			return true;

		} catch (Exception e) {
			// e.printStackTrace();
			return false;
		}
	}

	/**
	 * Fecha a conecção do Banco
	 */
	public void closeConnection() {
		if (hasConnection()) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Fecha o Estado da Query
	 */
	protected void closeState() {
		try {
			if (result != null) {
				statement.close();
				this.statement = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fecha o Resultado da Query
	 */
	protected void closeResult() {
		try {
			if (result != null) {
				result.close();
				this.result = null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cria uma connecção com a Database
	 * 
	 * @return
	 * @throws Exception
	 */
	public Connection connectBase() throws Exception {
		return DriverManager.getConnection(getURL() + database, user, pass);
	}

	/**
	 * Cria uma conneção com o Driver
	 * 
	 * @return
	 * @throws Exception
	 */
	public Connection connect() throws Exception {
		if (useSQLite) {
			return DriverManager.getConnection("jdbc:sqlite:" + database);
		} else {
			return DriverManager.getConnection(getURL(), user, pass);
		}
	}

	/**
	 * Cria um Texto baseado nas variaveis
	 * 
	 * @return Texto estilo URL
	 */
	private String getURL() {
		return type + host + ":" + port + "/";
	}

	/**
	 * Abre a coneção com o banco de dados caso não exista ainda
	 * 
	 * @return Mesma instacia da classe DBManager
	 */
	public DBManager openConnection() {
		if (!hasConnection()) {
			try {
				this.connection = connect();
				if (!useSQLite) {
					createDatabase(database);
					useDatabase(database);
				}
			} catch (Exception e) {
				if (debug) {
					e.printStackTrace();
				}
			}
		}

		return this;
	}

	/**
	 * Ve se a conecção não esta nula
	 * 
	 * @return Se a coneção existe
	 */
	public boolean hasConnection() {
		return connection != null;
	}

	/**
	 * Volta a conecção da variavel
	 * 
	 * @return Conecção atual
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Construtor pedindo Usuario, Senha, Host sem conectar com nenhum database
	 * apenas no Driver
	 * 
	 * @param user
	 *            Usuario
	 * @param pass
	 *            Senha
	 * @param host
	 *            Host
	 */
	public DBManager(String user, String pass, String host) {
		this(user, pass, host, "mine");
	}

	/**
	 * Contrutor pedindo Usuario, Senha, Host, Database
	 * 
	 * @param user
	 *            Usuario
	 * @param pass
	 *            Senha
	 * @param host
	 *            Host
	 * @param database
	 *            Database
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
	 * @param database
	 *            Database
	 */
	public void createDatabase(String database) {
		update("create database if not exists " + database
				+ " default character set utf8 default collate utf8_general_ci");
	}

	/**
	 * Conecta com a database
	 * 
	 * @param database
	 *            Database
	 */
	public void useDatabase(String database) {
		update("USE " + database);
	}

	/**
	 * Cria uma tabela
	 * 
	 * @param table
	 *            Tabela
	 * @param values
	 *            Valores
	 */
	public void createTable(String table, String values) {
		if (useSQLite) {
			update("CREATE TABLE IF NOT EXISTS " + table + " (ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , " + values
					+ ");");
		} else {
			update("CREATE TABLE IF NOT EXISTS " + table + " (ID INT NOT NULL AUTO_INCREMENT, " + values
					+ ", PRIMARY KEY(ID)) default charset = utf8");
		}
	}

	/**
	 * Deleta todas tabelas da database
	 * 
	 * @param database
	 *            Database
	 */
	public void clearDatabase(String database) {
		// update("TRUNCATE DATABASE " + database);
	}

	/**
	 * Deleta database
	 * 
	 * @param database
	 *            Database
	 */
	public void deleteDatabase(String database) {
		update("DROP DATABASE " + database);
	}

	/**
	 * Insere um registro
	 * 
	 * @param table
	 *            Tabela
	 * @param objects
	 *            Objetos
	 */
	public void insert(String table, Object... objects) {
		if (useSQLite) {
			update("INSERT INTO " + table + " values ( NULL , " + hashes(objects.length) + " )", objects);
		} else {
			update("INSERT INTO " + table + " values (default, " + inters(objects.length) + " )", objects);
		}
	}

	/**
	 * Deleta um registro
	 * 
	 * @param table
	 *            Tabela
	 * @param index
	 *            Index (ID)
	 */
	public void delete(String table, int index) {
		update("DELETE FROM " + table + " WHERE ID = ?", index);
	}

	/**
	 * Deleta um registro
	 * 
	 * @param table
	 *            Tablea
	 * @param where
	 *            Como
	 * @param values
	 *            Valores
	 */
	public void delete(String table, String where, Object... values) {
		update("DELETE FROM " + table + " WHERE " + where, values);
	}

	/**
	 * Deleta uma coluna
	 * 
	 * @param table
	 *            Tale
	 * @param column
	 *            Coluna
	 */
	public void delete(String table, String column) {
		alter(table, "drop column " + column);
	}

	/**
	 * Adiciona no Começo da tabela uma coluna
	 * 
	 * @param table
	 *            Tabela
	 * @param columnComplete
	 *            Coluna
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
	 * @param table
	 *            Tabela
	 * @param newTable
	 *            Nova tabela
	 */
	public void renameTable(String table, String newTable) {
		alter(table, "rename to " + newTable);
	}

	/**
	 * Modifica uma Coluna de uma Tabela
	 * 
	 * @param table
	 *            Tabela
	 * @param column
	 *            Coluna
	 * @param modification
	 *            Modificação
	 */
	public void modify(String table, String column, String modification) {
		alter(table, "modify column " + column + " " + modification);
	}

	/**
	 * Adiciona chave primaria na tabela
	 * 
	 * @param table
	 *            Tabela
	 * @param key
	 *            Chave
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
	 * @param table
	 *            Tabela
	 * @param where
	 *            Como
	 * @param edit
	 *            Modificação
	 * @param values
	 *            Valores
	 */
	public void change(String table, String edit, String where, Object... values) {
		update("UPDATE " + table + " SET " + edit + " WHERE " + where, values);
	}

	/**
	 * Cria um join entre as tabelas
	 * 
	 * @param table
	 *            Tabela
	 * @param joinTable
	 *            Tabela2
	 * @param onClause
	 *            Comparador
	 * @param select
	 *            Select completo
	 * @return ResultSet
	 */
	public ResultSet join(String table, String joinTable, String onClause, String select) {
		return select(select + " FROM " + table + " JOIN " + joinTable + " ON " + onClause);
	}

	/**
	 * Deleta a tabela
	 * 
	 * @param table
	 *            Tabela
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
	 * 
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
	 * @param query
	 *            Query
	 * @param replacers
	 *            Objetos
	 * @return Se tem ou não registro com esta Query
	 */
	public boolean contains(String query, Object... replacers) {
		boolean has = false;
		if (hasConnection())
			try {
				ResultSet rs = select(query, replacers);
				has = rs.next();
				closeSelect();

			} catch (Exception e) {
				e.printStackTrace();
			}
		return has;

	}

	/**
	 * Executa uma Atualização com um Query
	 * 
	 * @param query
	 *            Query Pesquisa
	 * @param replacers
	 *            Objetos
	 */
	public void update(String query, Object... replacers) {
		if (hasConnection())
			try {
				query(query, replacers).executeUpdate();
				closeSelect();
			} catch (Exception e) {
				e.printStackTrace();

			}
	}

	/**
	 * Cria um PreparedStatement com uma Query dada, e aplica os Replacers
	 * 
	 * @param query
	 *            Query
	 * @param replacers
	 *            Objetos
	 * @return PreparedStatement (Estado da Query)
	 */
	public PreparedStatement query(String query, Object... replacers) {
		try {

			if (!query.endsWith(";")) {
				query += ";";
			}
			if (useSQLite) {
				query = query.replaceAll("\\?", "#");
				for (Object replacer : replacers) {
					query = query.replaceFirst("#", "'" + replacer + "'");
				}
				System.out.println("[SQLite] " + query);
			}

			PreparedStatement state = connection.prepareStatement(query);
			if (!useSQLite) {
				int id = 1;
				for (Object replacer : replacers) {
					if (replacer == null) {
						state.setObject(id, replacer);
					} else if (replacer instanceof Date) {
						state.setDate(id, (Date) replacer);
					} else if (replacer instanceof Time) {
						state.setTime(id, (Time) replacer);
					} else if (replacer instanceof Timestamp) {
						state.setTimestamp(id, (Timestamp) replacer);
					} else {
						state.setString(id, "" + replacer);
					}
					id++;
				}
				query = query.replaceAll("\\?", "#");
				for (Object replacer : replacers) {
					query = query.replaceFirst("#", "'" + replacer + "'");
				}
				System.out.println("[MySQL] " + query);
			}

			// System.out.println(state.get);
			return statement = state;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getString(String table, String column, String where, Object... replacers) {
		String result = "";
		ResultSet rs = selectAll(table, where, replacers);
		try {
			if (rs.next()) {
				result = rs.getString(column);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeSelect();
		return result;
	}

	public Date getDate(String table, String column, String where, Object... replacers) {
		Date result = null;
		ResultSet rs = selectAll(table, where, replacers);
		try {
			if (rs.next()) {
				result = rs.getDate(column);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeSelect();
		return result;
	}

	public UUID getUUID(String table, String column, String where, Object... replacers) {
		return UUID.fromString(getString(table, column, where, replacers));
	}

	public int getInt(String table, String column, String where, Object... replacers) {
		int result = -1;
		ResultSet rs = selectAll(table, where, replacers);
		try {
			if (rs.next()) {
				result = rs.getInt(column);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeSelect();
		return result;
	}

	public double getDouble(String table, String column, String where, Object... replacers) {
		double result = -1;
		ResultSet rs = selectAll(table, where, replacers);
		try {
			if (rs.next()) {
				result = rs.getDouble(column);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeSelect();
		return result;
	}

	public ResultSet selectAll(String table, String where, Object... replacers) {
		return select("SELECT * FROM " + table + " WHERE " + where, replacers);
	}

	/**
	 * Executa um Query e volta um ResultSet
	 * 
	 * @param query
	 *            Pesquisa
	 * @param replacers
	 *            Objetos
	 * @return ResultSet (Resultado da Query)
	 */
	public ResultSet select(String query, Object... replacers) {
		try {
			return result = query(query, replacers).executeQuery();
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
	public Object restore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub

	}

	/**
	 * Gera um texto com "?" baseado na quantidade<br< Exemplo 5 = "? , ?,?,?,?"
	 * 
	 * @param size
	 *            Quantidade
	 * @return Texto criado
	 */
	public String inters(int size) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < size; i++) {
			if (i != 0)
				builder.append(",");
			builder.append("?");
		}
		return builder.toString();
	}

	public String hashes(int size) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < size; i++) {
			if (i != 0)
				builder.append(",");
			builder.append("#");
		}
		return builder.toString();
	}

	@Override
	public String toString() {
		return "DBManager [user=" + user + ", pass=" + pass + ", host=" + host + ", port=" + port + ", database="
				+ database + ", type=" + type + "]";
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		DBManager.debug = debug;
	}

	public boolean useSQLite() {
		return useSQLite;
	}

	public void setUseSQLite(boolean useSQLite) {
		this.useSQLite = useSQLite;
	}

	/**
	 * Seleciona tudo que o Select volta e transforma em Lista de Mapa<br>
	 * Lista = Linhas<br>
	 * Mapa = Colunas<br>
	 * 
	 * @param query
	 *            Query
	 * @param replacers
	 *            Objetos
	 * @return Lista de Mapa
	 */
	public List<Map<String, String>> getResult(String query, Object... replacers) {
		List<Map<String, String>> list = new ArrayList<>();
		try {
			ResultSet rs = select(query);
			while (rs.next()) {
				Map<String, String> mapa = new HashMap<>();
				ResultSetMetaData meta = rs.getMetaData();
				for (int i = 1; i <= meta.getColumnCount(); i++) {
					String name = meta.getColumnName(i);
					mapa.put(name, rs.getString(name));
				}
				list.add(mapa);
			}
			closeSelect();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Map<String, Object>> selectAll(String table, Object... replacers) {
		List<Map<String, Object>> lista = new ArrayList<>();
		try {
			ResultSet rs = select("SELECT * FROM " + table, replacers);
			while (rs.next()) {
				Map<String, Object> mapa = new HashMap<>();
				ResultSetMetaData meta = rs.getMetaData();
				for (int colunaId = 1; colunaId <= meta.getColumnCount(); colunaId++) {
					String name = meta.getColumnName(colunaId);
					mapa.put(name, rs.getObject(name));
				}
				lista.add(mapa);
			}
			rs.getStatement().getConnection().close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	public Map<String, Object> selectOne(String table, Object... replacers) {
		Map<String, Object> mapa = new HashMap<>();
		try {
			ResultSet rs = select("SELECT * FROM " + table, replacers);
			if (rs.next()) {
				ResultSetMetaData meta = rs.getMetaData();
				for (int colunaId = 1; colunaId <= meta.getColumnCount(); colunaId++) {
					String name = meta.getColumnName(colunaId);
					mapa.put(name, rs.getObject(name));
				}
			}
			rs.getStatement().getConnection().close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mapa;
	}


}
