package net.eduard.api.lib.storage.references;

import java.util.List;

import net.eduard.api.lib.storage.StorageAPI;

public class ReferenceList extends ReferenceBase<List<Integer>> {



	public ReferenceList(List<Integer> listOfReferences, Object listOriginal) {
		super(listOfReferences, null, listOriginal);

	}

	@Override
	public void update() { 
		@SuppressWarnings("unchecked")
		List<Object> newList = (List<Object>) getInstance();
		for (Integer item : getRestore()) {
			newList.add(StorageAPI.getObjectById(item));
		}

	}



}
