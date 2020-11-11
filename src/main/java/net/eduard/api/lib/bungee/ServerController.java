package net.eduard.api.lib.bungee;

public interface ServerController {

	void sendMessage(String server, String tag, String line);
	void sendMessage(String tag, String line);
	void receiveMessage(String server, String tag, String line);
	void register();
	void unregister();
	void connect(String player, String serverType, ServerState state);
	void setState(String server,ServerState state);

	

}
