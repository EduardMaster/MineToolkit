package net.eduard.api.lib.config;

import java.io.File;

public class AIOConfig {
	
	private AIOObject data;
	
	
	
	
	public void load(File file) {
		
	}
	
	public void saveAsYAML(File file) {
		save(file, AIOType.YAML);
	}
	
	public void save(File file,AIOType type) {
		
	}

	public AIOObject getData() {
		return data;
	}

	public void setData(AIOObject data) {
		this.data = data;
	}

}
