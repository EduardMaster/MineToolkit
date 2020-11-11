package net.eduard.api.lib.bungee;

public interface ServerController<T> {

	void sendMessage(String server, String tag, String line);
	void sendMessage(String tag, String line);
	void receiveMessage(String server, String tag, String line);
	void register(T plugin);
	void unregister();
	void connect(String player, String serverType, String subType);
	void setState(String server,ServerState state);

	

}
