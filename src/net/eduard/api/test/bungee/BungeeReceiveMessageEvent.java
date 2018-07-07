package net.eduard.api.test.bungee;

import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class BungeeReceiveMessageEvent extends Event implements Cancellable{
	private boolean cancelled;
	private ServerInfo server;
	private String[] args;

	public String[] getArgs() {
		return args;
	}

	public void setArgs(String... args) {
		this.args = args;
	}

	public ServerInfo getServer() {
		return server;
	}

	public void setServer(ServerInfo server) {
		this.server = server;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

}
