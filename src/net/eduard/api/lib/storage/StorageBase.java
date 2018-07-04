package net.eduard.api.lib.storage;

import java.lang.reflect.Field;

public abstract class StorageBase {
	private static boolean debug = true;
	private Class<?> type;
	private boolean reference;
	private Field field;
	private Object instance;
	public StorageBase(Field field,Object instance,Class<?> type, boolean asReference) {
		this(type, asReference);
		setField(field);
		setInstance(instance);
	}
	public void debug(String msg) {
		if (debug)
			System.out.println("[Storage] " + msg);
	}

	public static void setDebug(boolean debug) {
		StorageBase.debug = debug;
	}
	public StorageBase(Class<?> type, boolean asReference) {
		super();
		this.type = type;
		this.reference = asReference;
		
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

	public abstract Object restore(Object data);

	public abstract Object store(Object data);

	public boolean isReference() {
		return reference;
	}

	public void setReference(boolean reference) {
		this.reference = reference;
	}

	public Object getInstance() {
		return instance;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}
}
