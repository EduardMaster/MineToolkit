package net.eduard.api.lib.database;

import net.eduard.api.lib.modules.Extra;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Banco de dados automatico
 * 
 * @author Eduard
 * @version 1.0
 * @since Lib v2.0
 */
public interface AutoBase {

	/**
	 * Informacao da Coluna
	 * 
	 * @author Eduard
	 * 
	 * @version 1.0
	 * @since Lib v2.0
	 *
	 */
	@Target({ ElementType.FIELD, ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Info {

		String name() default "";

		int size() default 11;

		boolean primary() default false;

		boolean secondary() default false;

		boolean unique() default false;

		boolean nullable() default false;

		String type() default "";

		String charset() default "UTF8";
	}

	public default Connection connect() {

		return null;
	}

	public default boolean debug() {
		return true;
	}

	public default void debug(String msg) {
		if (debug())
			System.out.println("[AutoBase] " + msg);
	}

	public default String tableName() {
		if (getClass().isAnnotationPresent(Info.class)) {
			Info info = getClass().getAnnotation(Info.class);
			return info.name();
		}
		return getClass().getSimpleName();
	}

	public default String charset() {
		if (getClass().isAnnotationPresent(Info.class)) {
			Info info = getClass().getAnnotation(Info.class);
			return info.charset();
		}
		return "UTF8";
	}

	default Field getPrimaryKey() {
		for (Field field : getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Info.class)) {
				Info info = field.getAnnotation(Info.class);
				if (info.primary()) {
					field.setAccessible(true);
					return field;
				}
			}
		}
		return null;
	}

	default Field getSecondaryKey() {
		for (Field field : getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(Info.class)) {
				Info info = field.getAnnotation(Info.class);
				if (info.secondary()) {
					field.setAccessible(true);
					return field;
				}
			}
		}
		return null;
	}

	default String getFieldName(Field field, String defaultName) {
		if (field != null) {
			if (field.isAnnotationPresent(Info.class)) {
				Info info = field.getAnnotation(Info.class);
				return info.name().isEmpty() ? field.getName() : info.name();
			}
		}
		return defaultName;
	}

	default String primaryKey() {
		return getFieldName(getPrimaryKey(), "id");

	}

	default String secondaryKey() {
		return getFieldName(getSecondaryKey(), "name");
	}

	public default void delete() {
		delete(connect());
	}

	public default void insert() {
		insert(connect());

	}

	/**
	 * Criar um table com a Conexão padrão
	 */
	public default void createTable() {
		createTable(connect());
	}

	default PreparedStatement query(String sql, Object... replacers) throws SQLException {
		return query(connect(), sql, replacers);
	}

	default PreparedStatement query(Connection connection, String sql, Object... replacers) throws SQLException {
		PreparedStatement prepareStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		int id = 1;
		for (Object replacer : replacers) {
			Extra.setSQLValue(prepareStatement, id, replacer);
			sql = sql.replaceFirst("\\?", "'" + Extra.fromJavaToSQL(replacer) + "'");
			id++;
		}
		debug(sql);
		return prepareStatement;

	}

	default int update(String query, Object... replacers) {
		return update(connect(), query, replacers);
	}

	default String getTableName() {
		return tableName();
	}

	default int update(Connection connection, String query, Object... replacers) {
		int resultado = 1;

		try {
			PreparedStatement state = query(connection, query, replacers);
			resultado = state.executeUpdate();
			ResultSet keys = state.getGeneratedKeys();
			if (keys != null) {
				if (keys.next()) {
					resultado = keys.getInt(1);
				}
			}
			state.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultado;
	}

	default ResultSet select(Connection connection, String query, Object... replacers) throws SQLException {
		return query(connection, query, replacers).executeQuery();

	}

	default ResultSet select(String query, Object... replacers) throws SQLException {
		return query(connect(), query, replacers).executeQuery();

	}

	/**
	 * Atualiza os dados do java no banco de dados usando conexao padrão
	 */
	public default void updateTable() {
		updateTable(connect());
	}

	public default void updateTable(Connection connection) {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("UPDATE " + tableName() + " SET ");

			List<Object> list = new ArrayList<>();
			boolean first = true;
			for (Field field : getClass().getDeclaredFields()) {
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
				Object valor = field.get(this);
				if (field.isAnnotationPresent(Info.class)) {
					Info options = field.getAnnotation(Info.class);
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
				builder.append(getFieldName(field) + " = ? ");
				list.add(valor);

			}
			builder.append(" WHERE " + getFieldName(getPrimaryKey()) + " = ? ;");
			list.add(getPrimaryKey().get(this));
			update(connection, builder.toString(), list.toArray(new Object[list.size()]));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public default void insert(Connection connection) {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("INSERT INTO " + tableName() + " VALUES (");
			boolean first = true;
			List<Object> list = new ArrayList<>();

			for (Field field : getClass().getDeclaredFields()) {
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

				field.setAccessible(true);
				Object valor = field.get(this);
				if (field.isAnnotationPresent(Info.class)) {
					Info options = field.getAnnotation(Info.class);

					if (options.primary()) {
						builder.append(" DEFAULT");
						continue;
					}
					if (!options.nullable() && valor == null) {
						builder.append(" ''");
						continue;
					}
				}
				builder.append(" ? ");
				list.add(valor);

			}
			builder.append(")");

			int resultado = 1;

			resultado = update(connection, builder.toString(), list.toArray(new Object[list.size()]));

			getPrimaryKey().set(this, resultado);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public default void delete(Connection connection) {
		try {
			Field primary = getPrimaryKey();
			Object valor = primary.get(this);
			update(connection, "DELETE FROM " + tableName() + " WHERE " + primaryKey() + " = " + valor);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public default void deleteTable(Connection connection) {
		try {
			update(connection, "DROP TABLE " + tableName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public default List<? extends AutoBase> getAll(Connection connection, boolean decending) {
		return getAll(this, connection, primaryKey(), decending);
	}

	public default List<? extends AutoBase> getAll(boolean decending) {
		return getAll(connect(), decending);
	}

	public default List<? extends AutoBase> getAll(String collumnOrdened, boolean decending) {
		return getAll(this, connect(), collumnOrdened, decending);
	}

	@SuppressWarnings({ "unchecked" })
	public static <E extends AutoBase> List<E> getAll(E e, Connection connection, String collumnOrdened,
			boolean decending) {
		List<E> lista = new ArrayList<>();
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("SELECT * FROM " + e.getTableName() + " ORDER BY ? " + ((decending) ? "DESC" : "ASC") + ";");
			Field var = e.getClass().getDeclaredField(collumnOrdened);
			var.setAccessible(true);
			ResultSet rs = e.select(connection, builder.toString(), e.getFieldName(var));
			while (rs.next()) {
				E newE = null;
				try {

					newE = (E) e.getClass().newInstance();

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
					Object value = Extra.getSQLValue(rs, field.getType(), e.getFieldName(field));
					field.set(newE, value);
				}
				lista.add(newE);
			}
			rs.getStatement().close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}

	/**
	 * Puxa os dados para memória usando Conexão padrão
	 * 
	 * @return Se puxou ou não os dados
	 */
	public default boolean updateCache() {
		return updateCache(connect());
	}

	/**
	 * Puxa os dados para memória usando uma Conexão
	 * 
	 * @param connection Conexão
	 * @return Se puxou ou não os dados
	 */
	public default boolean updateCache(Connection connection) {
		boolean hasUpdateCode = false;
		try {
			Field primary = getPrimaryKey();
			String primaryName = getFieldName(primary);
			Field secondary = getSecondaryKey();
			String updaterName = getFieldName(secondary);
			StringBuilder builder = new StringBuilder();
			builder.append(
					"SELECT * FROM " + tableName() + " WHERE " + primaryName + " = ? OR " + updaterName + " = ? ;");
			Object secondaryValue = secondary.get(this);
			Object primaryValue = primary.get(this);

			ResultSet rs = select(connection, builder.toString(), primaryValue, secondaryValue);

			if (rs.next()) {
				hasUpdateCode = true;
				for (Field field : getClass().getDeclaredFields()) {
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
					Object value = Extra.getSQLValue(rs, field.getType(), getFieldName(field));
					field.set(this, value);
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
	 * Criar uma tabela
	 * 
	 * @param connection Conexão
	 */
	public default void createTable(Connection connection) {
		StringBuilder builder = new StringBuilder();

		builder.append("CREATE TABLE IF NOT EXISTS " + tableName() + " (");
		Class<?> claz = getClass();
		boolean first = true;
		for (Field field : claz.getDeclaredFields()) {

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
			builder.append(" " + getFieldName(field));
			builder.append(" " + getSQLType(field));

		}
		builder.append(")");
		builder.append(" default charset = ");
		builder.append(charset());

		builder.append(";");

		update(connection, builder.toString());

		// public void createTable(String table, String values) {
		// update("CREATE TABLE IF NOT EXISTS " + table
		// + " (ID INT NOT NULL AUTO_INCREMENT , " + values
		// + ", PRIMARY KEY(ID)) default charset = utf8");
		// }
	}

	/**
	 * Pega o nome da variavel
	 * 
	 * @param field Variavel
	 * @return Nome da variavel
	 */
	default String getFieldName(Field field) {
		return getFieldName(field, field.getName());
	}

	/**
	 * Pega o tipo SQL de um tipo Java
	 * 
	 * @param field Variavel java
	 * @return Tipo SQL
	 */
	static String getSQLType(Field field) {
		StringBuilder builder = new StringBuilder();
		try {
			field.setAccessible(true);
			Class<?> typeClass = field.getType();

			boolean unique = false;
			boolean nullable = false;
			boolean primary = false;
			String type = Extra.getSQLType(typeClass, 11);
			if (field.isAnnotationPresent(Info.class)) {
				Info options = field.getAnnotation(Info.class);
				nullable = options.nullable();
				primary = options.primary();
				unique = options.unique();
				type = Extra.getSQLType(typeClass, options.size());

				if (!options.type().isEmpty()) {
					type = options.type();
				}

			}
//		
			builder.append("" + type);

			if (!nullable) {
				builder.append(" NOT NULL");
			}
			if (primary) {
				builder.append(" AUTO_INCREMENT PRIMARY KEY");
			}
			if (unique) {
				builder.append(" UNIQUE");
			}

		} catch (Exception e) {
			e.printStackTrace();
			builder.append("VARCHAR(100) NOT NULL");
		}
		// System.out.println(builder.toString());
		return builder.toString();
	}

	/**
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	/**
	 * 
	 * @author Eduard
	 * @version 1.0
	 * @since Lib v1.1
	 */
	public static class VarInfo {
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

	public default Map<String, VarInfo> getVariableFromTable(Connection connection, String query, Object... replacers) {
		return getVariablesFromTable(connection, query, replacers).stream().findFirst().orElse(new HashMap<>());
	}

	public default List<Map<String, VarInfo>> getVariablesFromTable(Connection connection, String query,
			Object... objects) {
		List<Map<String, VarInfo>> lista = new LinkedList<>();

		try {
			ResultSet rs = select(connection, query, objects);
			ResultSetMetaData meta = rs.getMetaData();

			while (rs.next()) {
				Map<String, VarInfo> mapa = new LinkedHashMap<>();
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
					VarInfo campo = new VarInfo();
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

}