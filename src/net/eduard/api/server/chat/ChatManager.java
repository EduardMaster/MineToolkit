package net.eduard.api.server.chat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.eduard.api.lib.storage.Mine;
import net.eduard.api.lib.storage.manager.EventsManager;

public class ChatManager extends EventsManager  {
	private String messageChatDisabled = "&cChat desabilitado tempariamente!";
	private String messageChatPermission = "§cVocê não tem permissão para falar neste Chat!";
	private ChatChannel chatDefault = new ChatChannel("local", "", "§e§l(L) ", "", "l");
	private ChatType chatType = ChatType.BUKKIT;
	private boolean chatEnabled;

	private Map<String, ChatChannel> channels = new HashMap<>();

	public void register(ChatChannel channel) {
		channels.put(channel.getName().toLowerCase(), channel);
	}

	public void unregister(ChatChannel channel) {
		channels.remove(channel.getName().toLowerCase());
	}
	public ChatChannel getChatDefault() {
		return chatDefault;
	}
	

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		chatDefault.chat(event.getPlayer(), event.getMessage(),chatType);
	}

	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
		String msg = event.getMessage();
		String cmd = Mine.getCmd(msg);
		for (ChatChannel channel : channels.values()) {
			if (Mine.startWith(cmd, "/" + channel.getName())) {
				channel.chat(event.getPlayer(), msg.replaceFirst(cmd, ""),chatType);
				event.setCancelled(true);
				break;
			}
			if (Mine.startWith(cmd, "/" + channel.getCommand())) {
				channel.chat(event.getPlayer(), msg.replaceFirst(cmd, ""),chatType);
				event.setCancelled(true);
				break;
			}
		}
	}
	public void setChatDefault(ChatChannel chatDefault) {
		this.chatDefault = chatDefault;
	}

	public Map<String, ChatChannel> getChannels() {
		return channels;
	}

	public void setChannels(Map<String, ChatChannel> channels) {
		this.channels = channels;
	}

	public ChatType getChatType() {
		return chatType;
	}

	public void setChatType(ChatType chatType) {
		this.chatType = chatType;
	}

	public String getMessageChatPermission() {
		return messageChatPermission;
	}

	public void setMessageChatPermission(String messageChatPermission) {
		this.messageChatPermission = messageChatPermission;
	}

	public String getMessageChatDisabled() {
		return messageChatDisabled;
	}

	public void setMessageChatDisabled(String messageChatDisabled) {
		this.messageChatDisabled = messageChatDisabled;
	}

	public boolean isChatEnabled() {
		return chatEnabled;
	}

	public void setChatEnabled(boolean chatEnabled) {
		this.chatEnabled = chatEnabled;
	}

}
