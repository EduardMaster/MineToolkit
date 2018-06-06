package net.eduard.api.lib.storage.references;

import java.lang.reflect.Field;

public abstract class ReferenceAbstract {

	private Field field;
	private Object instance;
	




	public ReferenceAbstract(Field field, Object instance) {
		super();
		this.field = field;
		this.instance = instance;
	}


	public abstract void update();
	
	
	public Object getObjectById(int id) {
		
		return null;
	}
	
	

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}




	public Object getInstance() {
		return instance;
	}




	public void setInstance(Object instance) {
		this.instance = instance;
	}
}
