package net.eduard.api.lib.autobase;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 * Banco de dados automatico
 * 
 * @author Eduard
 * @version 1.0
 * @since Lib v2.0
 */
public interface AutoBase {
	public default Connection connect() {

		return null;
	}

	public default ResultSet select(String query) {
		return null;

	}

	public default String defaultCharset() {
		return "utf8";
	}

	public default Map<String, VarInfo> getOne(String query, Object... replacers) {
		Map<String, VarInfo> mapa = new LinkedHashMap<>();
		try {
			ResultSet rs = select(query, replacers);
			ResultSetMetaData meta = rs.getMetaData();
			if (rs.next()) {
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeSelect();
		return mapa;
	}

	public default ResultSet select(String query, Object... replacers) {
		return null;

	}

	public default void closeSelect() {
	}

	public default List<Map<String, VarInfo>> getAll(String query, Object... objects) {
		List<Map<String, VarInfo>> lista = new LinkedList<>();

		try {
			ResultSet rs = select(query);
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

		} catch (Exception e) {
			e.printStackTrace();
		}
		closeSelect();

		return lista;

	}

	// public void change(AutoBase base) {
	// base.change(getConnection());
	// }
	//
	// public void update(AutoBase base) {
	// base.update(getConnection());
	// }
	//
	// public void delete(AutoBase base) {
	// base.delete(getConnection());
	// }
	//
	// public <E extends AutoBase> List<E> getAll(E base, String collumn, boolean
	// decending) {
	// return base.getAll(base, collumn, decending, connection);
	// }

	// public <E extends AutoBase> List<E> getAll(E base) {
	// return base.getAll(base, base.getPrimaryKey().getName(), false, connection);
	// }
	// public <E extends AutoBase> List<E> getAll(Class<E> clazz) {
	// E instance = null;
	// try {
	// instance = clazz.newInstance();
	// } catch (InstantiationException | IllegalAccessException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// return null;
	// }
	// return instance.getAll(instance, instance.getPrimaryKey().getName(), false,
	// connection);
	// }
	// public void createTable(AutoBase base) {
	// base.createTable(getConnection());
	// }

	public default void change(Connection connection) {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("UPDATE " + getTableName() + " SET ");
			int x = 1;
			Map<Integer, Object> values = new HashMap<>();
			boolean first = true;
			for (Field field : getClass().getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				if (Modifier.isAbstract(field.getModifiers())) {
					continue;
				}

				field.setAccessible(true);
				Object valor = field.get(this);
				if (field.isAnnotationPresent(Info.class)) {
					Info options = field.getAnnotation(Info.class);
					// Class<?> type = field.getType();
					if (options.primaryKey()) {
						continue;
					}
					if (!options.canBeNull() && valor == null) {
						continue;
					}
				}
				if (!first) {
					builder.append(", ");
				} else {
					first = false;
				}
				builder.append(getFieldName(field) + " = ? ");
				values.put(x++, valor);

			}
			builder.append(" WHERE " + getFieldName(getPrimaryKey()) + " = ? ;");
			// System.out.println(getPrimaryKey().get(this));
			values.put(x++, getPrimaryKey().get(this));
			PreparedStatement st = connection.prepareStatement(builder.toString());
			for (Entry<Integer, Object> entry : values.entrySet()) {
				Integer id = entry.getKey();
				Object value = entry.getValue();
				st.setObject(id, value);
			}
			st.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public default void insert(Connection connection) {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("INSERT INTO " + getTableName() + " VALUES (");
			boolean first = true;
			for (Field field : getClass().getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				if (Modifier.isAbstract(field.getModifiers())) {
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
					// Class<?> type = field.getType();
					if (options.primaryKey()) {
						builder.append(" DEFAULT");
						continue;
					}
					if (!options.canBeNull() && valor == null) {
						builder.append(" ''");
						continue;
					}
				}
				if (valor != null) {
					builder.append(" '" + valor + "' ");
				} else {
					builder.append(" NULL ");
				}

			}
			builder.append(")");
			PreparedStatement st = connection.prepareStatement(builder.toString());
			st.executeUpdate();
			st.close();
			// getPrimaryKey().set(this, rs.getObject(getFieldName(getPrimaryKey())));
			// rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		restorePrimaryKey();

	}

	public default void insert() {
		insert(connect());

	}

	public default Field getUpdaterField() {
		for (Field field : getClass().getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			if (Modifier.isAbstract(field.getModifiers())) {
				continue;
			}
			field.setAccessible(true);
			if (field.isAnnotationPresent(Info.class)) {
				Info options = field.getAnnotation(Info.class);
				if (options.update()) {
					return field;
				}
			}
		}
		return null;

	}

	public default Field getPrimaryKey() {
		for (Field field : getClass().getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			if (Modifier.isAbstract(field.getModifiers())) {
				continue;
			}
			field.setAccessible(true);
			if (field.isAnnotationPresent(Info.class)) {
				Info options = field.getAnnotation(Info.class);
				if (options.primaryKey()) {
					return field;
				}
			}
		}

		return null;
	}

	public default void delete() {
		delete(connect());
	}

	public default void delete(Connection connection) {
		try {
			Field primary = getPrimaryKey();
			Object valor = primary.get(this);

			PreparedStatement st = connection.prepareStatement(
					"DELETE FROM " + getTableName() + " WHERE " + getFieldName(primary) + " = '" + valor + "';");
			st.executeUpdate();
			st.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public default List<AutoBase> getAll() {
		return getAll(this);
	}

	public default List<AutoBase> getAll(String collumnOrdened, boolean decending) {
		return getAll(this, collumnOrdened, decending, connect());
	}

	public default <E extends AutoBase> List<E> getAll(E e) {
		return getAll(e, getPrimaryKey().getName(), false, connect());
	}

	@SuppressWarnings({ "unchecked" })
	public default <E extends AutoBase> List<E> getAll(E e, String collumnOrdened, boolean decending,
			Connection connection) {
		List<E> lista = new ArrayList<>();
		try {
			// Field primary = getPrimaryKey();
			// String primaryName = getFieldName(primary);
			// Field updater = getUpdaterField();
			// String updaterName = getFieldName(updater);
			StringBuilder builder = new StringBuilder();

			builder.append("SELECT * FROM " + getTableName() + " ORDER BY ? " + ((decending) ? "DESC" : "ASC") + ";");
			System.out.println(builder.toString());
			PreparedStatement st = connection.prepareStatement(builder.toString());

			Field var = getClass().getDeclaredField(collumnOrdened);
			var.setAccessible(true);

			st.setObject(1, getFieldName(var));
			ResultSet rs = st.executeQuery();
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

					field.setAccessible(true);
					Object value = rs.getObject(getFieldName(field));
					field.set(newE, value);
				}
				lista.add(newE);
			}
			rs.close();
			st.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}

	/**
	 * Puxa os dados para mem�ria usando conex�o padr�o
	 * 
	 * @return Se puxou ou n�o os dados
	 */
	public default boolean update() {
		return update(connect());
	}

	/**
	 * Puxa os dados para mem�ria usando uma Conex�o
	 * 
	 * @param connection
	 *            Conex�o
	 * @return Se puxou ou n�o os dados
	 */
	public default boolean update(Connection connection) {
		boolean hasUpdateCode = false;
		try {
			Field primary = getPrimaryKey();
			String primaryName = getFieldName(primary);
			Field updater = getUpdaterField();
			String updaterName = getFieldName(updater);
			StringBuilder builder = new StringBuilder();
			builder.append(
					"SELECT * FROM " + getTableName() + " WHERE " + primaryName + " = ? OR " + updaterName + " = ? ;");

			PreparedStatement st = connection.prepareStatement(builder.toString());
			Object updaterValue = updater.get(this);
			Object primaryValue = primary.get(this);
			if (primaryValue != null) {
				if (primaryValue instanceof UUID) {
					primaryValue = "" + primaryValue;

				}
			}
			if (updaterValue != null) {
				if (updaterValue instanceof UUID) {
					updaterValue = "" + updaterValue;

				}
			}
			st.setObject(1, primaryValue);
			st.setObject(2, updaterValue);
			System.out.println("[MySQL] " + builder.toString());
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				hasUpdateCode = true;
				for (Field field : getClass().getDeclaredFields()) {
					if (Modifier.isStatic(field.getModifiers())) {
						continue;
					}
					if (Modifier.isAbstract(field.getModifiers())) {
						continue;
					}

					field.setAccessible(true);
					Object value = rs.getObject(getFieldName(field));
					field.set(this, value);
				}
			}
			rs.close();
			st.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hasUpdateCode;
	}

	/**
	 * Pega a chave primaria da tabela usando uma conex�o padr�o
	 * 
	 */
	public default void restorePrimaryKey() {
		createTable(connect());
	}

	/**
	 * Pega a chave primaria da tabela usando uma conex�o
	 * 
	 * @param connection
	 */
	public default void restorePrimaryKey(Connection connection) {

		try {
			Field primary = getPrimaryKey();
			String primaryName = getFieldName(primary);
			Field updater = getUpdaterField();
			String updaterName = getFieldName(updater);
			if (primary.get(this) == null || Integer.valueOf(0).equals(primary.get(this))) {
				StringBuilder builder = new StringBuilder();
				builder.append("SELECT * FROM " + getTableName() + " WHERE " + updaterName + " = ?" + ";");
				// System.out.println(builder.toString());
				PreparedStatement st = connection.prepareStatement(builder.toString());
				st.setObject(1, updater.get(this));
				ResultSet rs = st.executeQuery();
				if (rs.next()) {
					Object value = rs.getObject(primaryName);
					primary.set(this, value);
				}
				rs.close();
				st.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Atualiza os dados do java no banco de dados usando conexao padr�o
	 */
	public default void change() {
		change(connect());
	}

	// public static <E> E get(Class<E> claz,String query){
	//
	// return null;
	// }
	//
	// public List<AutoBase> selectAll(String whereIf,Connection connection) {
	// return select(whereIf, "ORDER BY " + column);
	// }
	//
	// public List<AutoBase> selectAllOrdened(String whereIf, String
	// column,Connection connection) {
	//
	//
	// List<E> lista = new LinkedList<>();
	//
	// return lista;
	//
	// }

	/**
	 * Pega o nome da tabela
	 * 
	 * @return Nome da tabela
	 */
	public default String getTableName() {

		if (getClass().isAnnotationPresent(Info.class)) {
			Info info = getClass().getAnnotation(Info.class);
			return info.name();
		}
		return getClass().getSimpleName();
	}

	/**
	 * Criar um table com a conex�o padr�o
	 */
	public default void createTable() {
		createTable(connect());
	}

	/**
	 * Criar uma tabela
	 * 
	 * @param connection
	 *            Conex�o
	 */
	public default void createTable(Connection connection) {

		StringBuilder builder = new StringBuilder();

		builder.append("CREATE TABLE IF NOT EXISTS " + getTableName() + " (");
		Class<? extends AutoBase> claz = getClass();
		boolean first = true;
		for (Field field : claz.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				continue;
			}
			if (Modifier.isAbstract(field.getModifiers())) {
				continue;
			}
			if (!first) {
				builder.append(", ");
			} else {
				first = false;
			}
			builder.append(" " + getFieldName(field));
			builder.append(" " + getType(field));

		}
		builder.append(")");
		builder.append(" default charset = ");
		builder.append(defaultCharset());

		builder.append(";");
		// System.out.println(builder.toString());
		try {
			connection.prepareStatement(builder.toString()).executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// public void createTable(String table, String values) {
		// update("CREATE TABLE IF NOT EXISTS " + table
		// + " (ID INT NOT NULL AUTO_INCREMENT , " + values
		// + ", PRIMARY KEY(ID)) default charset = utf8");
		// }
	}

	/**
	 * Pega o nome da variavel
	 * 
	 * @param field
	 *            Variavel
	 * @return Nome da variavel
	 */
	public default String getFieldName(Field field) {
		if (field.isAnnotationPresent(Info.class)) {
			Info info = field.getAnnotation(Info.class);
			if (!info.name().equals("")) {
				return getTableName() + "_" + info.name();
			}
		}
		return getTableName() + "_" + field.getName();
	}

	/**
	 * Pega o tipo SQL de um tipo Java
	 * 
	 * @param field
	 *            Variavel java
	 * @return Tipo SQL
	 */
	public default String getType(Field field) {
		StringBuilder builder = new StringBuilder();
		try {
			field.setAccessible(true);

			if (field.isAnnotationPresent(Info.class)) {
				Info options = field.getAnnotation(Info.class);
				Class<?> type = field.getType();
				if (type.equals(String.class)) {
					builder.append("VARCHAR");
				} else if (type.equals(Integer.class) || type.equals(int.class)) {
					builder.append("INT");
				} else if (type.equals(Double.class) || type.equals(double.class)) {
					builder.append("DOUBLE");
				} else if (type.equals(UUID.class)) {
					builder.append("VARCHAR");
				}
				builder.append("(" + options.size() + ")");
				if (!options.canBeNull()) {
					builder.append(" NOT NULL");
				}
				if (options.unique()) {
					builder.append(" UNIQUE");
				}
				if (options.primaryKey()) {
					builder.append(" AUTO_INCREMENT PRIMARY KEY");
				}

			} else {
				builder.append("VARCHAR(50) NOT NULL");
			}

		} catch (Exception e) {
			e.printStackTrace();
			builder.append("VARCHAR(100) NOT NULL");
		}
		// System.out.println(builder.toString());
		return builder.toString();
	}

}