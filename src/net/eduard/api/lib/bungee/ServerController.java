package net.eduard.api.lib.bungee;

public interface ServerController {
	public String getChannel();
	public void sendMessage(String server,String tag, String line);
	public void sendMessage(String tag, String line);
	public void receiveMessage(String server,String tag, String line);
	public void connect(String player, int serverType, int serverState);
	public default void connect(String player, int serverType, ServerState state) {
		connect(player, serverType, state.getValue());
	}
	public void setState(String server, int state);
	public void register();
	public void unregister();
	

}
