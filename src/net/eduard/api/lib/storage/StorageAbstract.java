package net.eduard.api.lib.storage;

public abstract class StorageAbstract {
	private Class<?> type;
	private boolean reference;

	public StorageAbstract(Class<?> type, boolean asReference) {
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
}
