package net.eduard.api.lib.storage;

import net.eduard.api.lib.storage.Storable.*;

import java.lang.reflect.Field;

public abstract class StorageBase<T> {

	private StorageInfo info;

	public StorageBase(StorageInfo info) {
		setInfo(info);
	}

	public abstract T restore(Object data);

	public abstract Object store(T data);



	public static void debug(String msg) {
		StorageAPI.debug(msg);
	}

	public void updateByType() {
		if (getType().isAnnotationPresent(StorageAttributes.class)) {
			StorageAttributes atr = getType().getAnnotation(StorageAttributes.class);
			update(atr);
		}
	}

	public void updateByStoreClass() {
		Storable store = getStore(getType());
		if (store != null) {
			if (store.getClass().isAnnotationPresent(StorageAttributes.class)) {
				StorageAttributes atr = store.getClass().getAnnotation(StorageAttributes.class);
				update(atr);
			}
		}
	}

	public void update(StorageAttributes atr) {
		setReference(atr.reference());
		setInline(atr.inline());
		setIndentifiable(atr.indentificate());
	}

	public void updateByField() {
		if (getField() != null) {
			if (getField().isAnnotationPresent(StorageAttributes.class)) {
				StorageAttributes atr = getField().getAnnotation(StorageAttributes.class);
				update(atr);
			}
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

	@Override
	public String toString() {
		return "StorageBase [isStorable()=" + isStorable() + ", getType()=" + getType() + ", isReference()="
				+ isReference() + ", isInline()=" + isInline() + ", isIndentifiable()=" + isIndentifiable() + "]";
	}

}
