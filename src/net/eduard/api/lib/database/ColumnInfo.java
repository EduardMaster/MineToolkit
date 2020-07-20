package net.eduard.api.lib.database;

import java.sql.Date;
import java.util.UUID;

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
