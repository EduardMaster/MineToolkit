package net.eduard.api.lib.storage.bukkit_storables;

import java.util.Map;

import org.bukkit.util.Vector;

import net.eduard.api.lib.storage.Storable;

public class VectorStorable implements Storable {

	@Override
	public Object restore(Map<String, Object> map) {
		return new Vector();
	}

	@Override
	public void store(Map<String, Object> map, Object object) {

	}

	@Override
	public boolean saveInline() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public Class<?> type() {
		return Vector.class;
	}

	@Override
	public String alias() {
		return "Vector";
	}
}
