package net.eduard.api.test.autobase;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.function.Function;

import javax.persistence.Table;

import org.bukkit.event.EventHandler;
import org.bukkit.scoreboard.Criterias;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

public class Test {

	public static String retornaTexto(String affs) {
		Function<String, String> a = Test::retornaTexto;
		return null;
	}
	public static void main(String[] args) {
		criarTabela(null, MeuObjeto.class);
	}

	public static void criarTabela(Connection connection, Class<?> claz) {
//		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("CREATE TABLE IF NOT EXISTS ");
		if (claz.isAnnotationPresent(Table.class)) {
			Table table = claz.getAnnotation(Table.class);
			stringBuilder.append(table.name());
		}else {
			stringBuilder.append(claz.getSimpleName());	
		}
		stringBuilder.append(" (");
		boolean first = true;
		for (Field field : claz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Column.class)) {
				Column column = field.getAnnotation(Column.class);

				if (!column.name().isEmpty()) {
					stringBuilder.append(column.name().replace(" ", "_"));
				} else {
					stringBuilder.append(field.getName().replace(" ", "_"));
				}
				stringBuilder.append(" ");
				if (!column.type().isEmpty()) {
					stringBuilder.append(column.type());
				} else {
					Class<?> type = field.getType();
					String typeName = getSQLType(type);
					stringBuilder.append(typeName);
				}
				
				stringBuilder.append("(");
				stringBuilder.append(column.size());
				stringBuilder.append(")");
				if (!column.nullable()) {
					stringBuilder.append(" NOT NULL ");
				}
				if (column.primary()) {
					stringBuilder.append(" AUTOINCREMENT");
				}
				stringBuilder.append(" ,");
			}
		}
		System.out.println(stringBuilder.toString());

	}

	public static String getSQLType(Class<?> type) {
		if (String.class.isAssignableFrom(type)) {
			return "VARCHAR";
		} else if (Integer.class.isAssignableFrom(type)) {
			return "INTERGER";
		} else if (Boolean.class.isAssignableFrom(type)) {
			return "BOOLEAN";
		} else if (Short.class.isAssignableFrom(type)) {
			return "SMALLINT";
		} else if (Byte.class.isAssignableFrom(type)) {
			return "TINYINT";
		} else if (Long.class.isAssignableFrom(type)) {
			return "LONG";
		} else if (Character.class.isAssignableFrom(type)) {
			return "CHAR";
		} else if (Float.class.isAssignableFrom(type)) {
			return "FLOAT";
		} else if (Double.class.isAssignableFrom(type)) {
			return "DOUBLE";
		} else if (Number.class.isAssignableFrom(type)) {
			return "NUMERIC";
		} else if (Date.class.isAssignableFrom(type)) {
			return "DATE";
		} else if (java.util.Date.class.isAssignableFrom(type)) {
			return "DATE";
		} else if (Time.class.isAssignableFrom(type)) {
			return "TIME";
		} else if (Calendar.class.isAssignableFrom(type)) {
			return "DATETIME";
		} else if (Timestamp.class.isAssignableFrom(type)) {
			return "TIMESTAMP";
		}

		return null;
	}

}
