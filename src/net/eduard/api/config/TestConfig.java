package net.eduard.api.config;

import java.io.File;

import net.eduard.api.config.master.MasterConfig;

public class TestConfig {
	public static void main(String[] args) {
		MasterConfig t = new MasterConfig(
				new File("F:/Youtube"), "teste.yml");
		t.saveResource(true);
		t.debug();																																																							
		t.reloadConfig();
		t.saveYaml();
//		t.saveConfig();
		t.debug();
		
	}

}
