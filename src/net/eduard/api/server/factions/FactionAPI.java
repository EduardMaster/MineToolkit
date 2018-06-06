package net.eduard.api.server.factions;

public class FactionAPI {
	private static FactionManager manager;
	

	public static FactionManager getManager() {
		return manager;
	}

	public static void setManager(FactionManager manager) {
		FactionAPI.manager = manager;
	}

}
