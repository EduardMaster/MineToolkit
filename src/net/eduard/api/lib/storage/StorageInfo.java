package net.eduard.api.lib.storage;

import java.lang.reflect.Field;

public class StorageInfo implements Cloneable {
	private Class<?> type;
	private boolean reference;
	private boolean inline;
	private boolean indentifiable;
	private Field field;
	
	public StorageInfo clone() {
		try {
			return (StorageInfo) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	public StorageInfo( Class<?> claz) {
		setType(claz);
	}
	public StorageInfo(Field field, Class<?> type, boolean asReference, boolean inline,boolean indentifiable) {
		setField(field);
		setInline(inline);
		this.type = type;
		this.reference = asReference;
		setIndentifiable(indentifiable);
	}

	public Class<?> getType() {
		return type;
	}

	public Storable getStore(Class<?> claz) {
		return StorageAPI.getStore(claz);
	}

	public String getAlias(Class<?> claz) {
		return StorageAPI.getAlias(claz);
	}

	public void setType(Class<?> type) {
		this.type = type;
	}


	public boolean isReference() {
		return reference;
	}

	public void setReference(boolean reference) {
		this.reference = reference;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public boolean isInline() {
		return inline;
	}

	public void setInline(boolean inline) {
		this.inline = inline;
	}

	public boolean isIndentifiable() {
		return indentifiable;
	}

	public void setIndentifiable(boolean indentifiable) {
		this.indentifiable = indentifiable;
	}
}
