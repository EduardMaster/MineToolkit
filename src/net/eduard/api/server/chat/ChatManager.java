package net.eduard.api.server.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import jdk.nashorn.internal.ir.annotations.Reference;
import net.eduard.api.lib.Mine;
import net.eduard.api.lib.manager.EventsManager;

public class ChatManager extends EventsManager  {
	private String format = "(channel) (player): (color) (message)";
	private String messageChatDisabled = "&cChat desabilitado tempariamente!";
	private String messageChatPermission = "�cVoc� n�o tem permiss�o para falar neste Chat!";
	@Reference
	private ChatChannel chatDefault; 
	private ChatType chatType = ChatType.BUKKIT;
	
	private boolean chatEnabled;
	

	private List<ChatChannel> channels = new ArrayList<>();
	private ArrayList<OfflinePlayer> tellDisabled = new ArrayList<>();
	private Map<OfflinePlayer, String> colors= new HashMap<>();

	public void register(ChatChannel channel) {
		if (channel.getFormat().isEmpty()) {
			channel.setFormat(format);
		}
		channels.add(channel);
	}

	public void unregister(ChatChannel channel) {
		channels.remove(channel);
	}
	public ChatChannel getChatDefault() {
		return chatDefault;
	}
	public ChatManager() {
		ChatChannel canal = new ChatChannel("local", "", "�e�l(L) ", "", "l");
		setChatDefault(canal);
		register(canal);
		register(new ChatChannel("global", "", "�6�l(G)", "", "g"));
		
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
		for (ChatChannel channel : channels) {
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

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Map<OfflinePlayer, String> getColors() {
		return colors;
	}

	public void setColors(Map<OfflinePlayer, String> colors) {
		this.colors = colors;
	}

	public ArrayList<OfflinePlayer> getTellDisabled() {
		return tellDisabled;
	}

	public void setTellDisabled(ArrayList<OfflinePlayer> tellDisabled) {
		this.tellDisabled = tellDisabled;
	}


}
