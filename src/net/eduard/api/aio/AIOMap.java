package net.eduard.api.aio;

import java.util.Map;

public class AIOMap extends AIOObject {

	private Map<String, AIOObject> map;

	public Map<String, AIOObject> getMap() {
		return map;
	}

	public void setMap(Map<String, AIOObject> map) {
		this.map = map;
	}
	
}
