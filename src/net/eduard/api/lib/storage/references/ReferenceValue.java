package net.eduard.api.lib.storage.references;

import java.lang.reflect.Field;

public class ReferenceValue extends ReferenceBase {

	private int objectId;


	public ReferenceValue(int id,Field field, Object instance) {
		super(field, instance);
		setObjectId(id);
	}

	@Override
	public void update() {
		try {
//			System.out.println(objectId);
//			System.out.println(getObjectById(objectId));
			getField().set(getInstance(), getObjectById(objectId));
//			System.out.println("setado "+getField().get(getInstance()));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getObjectId() {
		return objectId;
	}

	public void setObjectId(int objectId) {
		this.objectId = objectId;
	}

}
