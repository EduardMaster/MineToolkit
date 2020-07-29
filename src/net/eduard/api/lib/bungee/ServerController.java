package net.eduard.api.lib.bungee;

public interface ServerController {

	void sendMessage(String server, String tag, String line);
	void sendMessage(String tag, String line);
	void receiveMessage(String server, String tag, String line);
	void connect(String player, int serverType, int serverState);
	default void connect(String player, int serverType, ServerState state) {
		connect(player, serverType, state.getValue());
	}
	void setState(String server, int state);
	void register();
	void unregister();
	

}
