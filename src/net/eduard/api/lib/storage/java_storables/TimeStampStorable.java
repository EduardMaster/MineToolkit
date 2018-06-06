package net.eduard.api.lib.storage.java_storables;

import java.sql.Timestamp;

import net.eduard.api.lib.Extra;
import net.eduard.api.lib.storage.Storable;

public class TimeStampStorable implements Storable {
	@Override
	public boolean saveInline() {
		return true;
	}

	@Override
	public Object store(Object object) {
		if (object instanceof Timestamp) {
			Timestamp timestamp = (Timestamp) object;
			return timestamp.getTime();

		}
		return null;
	}

	@Override
	public Object restore(Object object) {
		if (object instanceof Long) {
			Long long1 = (Long) object;
			return new Timestamp(long1);
		}
		if (object instanceof String) {
			String string = (String) object;
			return new Timestamp(Extra.toLong(string));

		}
		return null;
	}

}
