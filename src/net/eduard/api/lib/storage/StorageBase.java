package net.eduard.api.lib.storage;

import java.lang.reflect.Field;

public abstract class StorageBase {

	private StorageInfo info;

	public StorageBase(StorageInfo info) {
		setInfo(info);
	}

	public static void debug(String msg) {
		StorageAPI.debug(msg);
	}
	public void update() {
		update(getField(),getType());
	}

	public void update(Field field, Class<?> claz) {
		Storable store = StorageAPI.getStore(claz);
		setReference(false);
		setInline(false);
		setIndentifiable(false);
		if (store != null) {
			claz = store.getClass();
		}
		if (claz.isAnnotationPresent(StorageAttributes.class)) {
			StorageAttributes atr = claz.getAnnotation(StorageAttributes.class);
			setReference(atr.reference());
			setInline(atr.inline());
			setIndentifiable(atr.indentificate());
//			Mine.console("§dClasse " + atr+ " §a"+claz);
		}
		if (field.isAnnotationPresent(StorageAttributes.class)) {
			StorageAttributes atr = field.getAnnotation(StorageAttributes.class);
			setReference(atr.reference());
			setInline(atr.inline());
			setIndentifiable(atr.indentificate());
//			Mine.console("§eField " + atr);
		}
	}

	public Storable getStore(Class<?> claz) {
		return StorageAPI.getStore(claz);
	}

	public Storable getStore() {
		return StorageAPI.getStore(getType());
	}

	public String getAlias(Class<?> claz) {
		return StorageAPI.getAlias(claz);
	}

	public String getAlias() {
		return getAlias(getType());
	}

	public abstract Object restore(Object data);

	public abstract Object store(Object data);

	public boolean isStorable() {
		return getStore() != null;
	}

	public StorageInfo getInfo() {
		return info;
	}

	public void setInfo(StorageInfo info) {
		this.info = info;
	}

	public Class<?> getType() {
		return info.getType();
	}

	public void setType(Class<?> type) {
		info.setType(type);
	}

	public boolean isReference() {
		return info.isReference();
	}

	public void setReference(boolean reference) {
		info.setReference(reference);
	}

	public Field getField() {
		return info.getField();
	}

	public void setField(Field field) {
		info.setField(field);
	}

	public boolean isInline() {
		return info.isInline();
	}

	public void setInline(boolean inline) {
		info.setInline(inline);
	}

	public boolean isIndentifiable() {
		return info.isIndentifiable();
	}

	public void setIndentifiable(boolean indentifiable) {
		info.setIndentifiable(indentifiable);
	}

	public boolean equals(Object obj) {
		return info.equals(obj);
	}

}
