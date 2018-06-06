package net.eduard.api.lib.storage;

public class StorageEnum extends StorageAbstract{
	
	public StorageEnum(Class<?> type) {
		super(type, false);
		
	}

	@Override
	public Object restore(Object data) {
		try {
			return getType().getDeclaredField(data.toString().toUpperCase()).get(null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Object store(Object data) {
		return data.toString();
	}

}
