package net.eduard.api.server.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ChatMessageEvent extends PlayerEvent implements Cancellable {
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	private static final HandlerList handlers = new HandlerList();
	private Map<String, String> tags = new HashMap<>();
	private String message = "";
	private String format;
	private String onClickCommand;
	private List<Player> playersInChannel = new ArrayList<>();
	private List<String> onHoverText;
	private boolean cancelled;
	private ChatChannel channel;

	public Map<String, String> getTags() {
		return tags;
	}

	public void setTagValue(String tag, String value) {
		tags.put(tag, value);
	}

	public String getTagValue(String tag) {
		return tags.get(tag);
	}

	public ChatMessageEvent(Player player, ChatChannel channel, String message) {
		super(player);
		this.message = message;
		if (player.hasPermission("chat.color"))
			this.message = ChatColor.translateAlternateColorCodes('&', message);
		setFormat(channel.getFormat());
		setPlayersInChannel(channel.getPlayers(player));
		resetTags();
	}

	public void resetTags() {
		for (int i = 0; i < format.length(); i++) {
			if (format.charAt(i) == '{') {
				String tag = format.substring(i + 1).split("}")[0].toLowerCase();
				if (!this.tags.containsKey(tag))
					this.tags.put(tag, "");
			}
		}
		for (int i = 0; i < format.length(); i++) {
			if (format.charAt(i) == '(') {
				String tag = format.substring(i + 1).split(")")[0].toLowerCase();
				if (!this.tags.containsKey(tag))
					this.tags.put(tag, "");
			}
		}
		
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		cancelled = cancel;

	}

	public ChatChannel getChannel() {
		return channel;
	}

	public void setChannel(ChatChannel channel) {
		this.channel = channel;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setTags(Map<String, String> tags) {
		this.tags = tags;
	}

	public String getOnClickCommand() {
		return onClickCommand;
	}

	public void setOnClickCommand(String onClickCommand) {
		this.onClickCommand = onClickCommand;
	}

	public List<String> getOnHoverText() {
		return onHoverText;
	}

	public void setOnHoverText(List<String> onHoverText) {
		this.onHoverText = onHoverText;
	}

	public List<Player> getPlayersInChannel() {
		return playersInChannel;
	}

	public void setPlayersInChannel(List<Player> playersInChannel) {
		this.playersInChannel = playersInChannel;
	}

}

