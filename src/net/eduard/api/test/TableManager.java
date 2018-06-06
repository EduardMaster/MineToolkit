package net.eduard.api.test;

import java.sql.Connection;
import java.util.List;

/**
 * Sistema compacto estilo Hibernate e Java Persistense
 * @author Eduard
 *
 * @param <E> Classe que representa a tabela
 */
public class TableManager<E> {
	
	public static enum TableManagerType{
		MYSQL, SQLITE;
	}
	private Connection connection;
	private TableManagerType connectionType;

	public List<E> getAll() {
		return null;
	}

	public E select(int id) {

		return null;
	}
	public boolean delete(int id) {
		
		return false;
	}
	public boolean delete(E e) {
		return false;
	}

	public boolean insert(E e) {

		return false;
	}

	public boolean contains(E e) {

		return false;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public TableManagerType getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(TableManagerType connectionType) {
		this.connectionType = connectionType;
	}

}
