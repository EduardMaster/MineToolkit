package net.eduard.api.lib.bungee;

public interface ServerMessageHandler {
	
	void onMessage(String server, String tag, String line);
	

}
