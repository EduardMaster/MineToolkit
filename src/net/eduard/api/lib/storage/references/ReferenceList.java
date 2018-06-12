package net.eduard.api.lib.storage.references;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReferenceList extends ReferenceBase{

	private List<Integer> list;
	
	public ReferenceList(List<Integer> list, Field field, Object instance) {
		super(field, instance);
		setList(list);
	}

	@Override
	public void update() {
		List<Object> newList = new ArrayList<>();
		for (Integer item : list) {
			newList.add(getObjectById(item));
		}
		try {
			getField().set(getInstance(), newList);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Integer> getList() {
		return list;
	}

	public void setList(List<Integer> list) {
		this.list = list;
	}

}
