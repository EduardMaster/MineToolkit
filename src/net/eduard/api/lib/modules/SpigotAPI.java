package net.eduard.api.lib.modules;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * API para utilização de metodos do Spigot com mais facilidade
 * 
 * @version 1.3
 * @since EduardAPI v1.0
 * @author Eduard
 *
 */
public final class SpigotAPI {
	/**
	 *
	 * 
	 * @param player
	 * @param message
	 * @param hoverMessage
	 * @param textToSend
	 */
	public static void sendMessage(Player player, String message, String hoverMessage, String textToSend,
			boolean runCommand) {
		sendMessage(Arrays.asList(player), message, Arrays.asList(hoverMessage), textToSend, runCommand);
	}

	/**
	 *
	 * 
	 * @param player
	 * @param message
	 * @param hoverMessage
	 * @param clickCommand
	 */
	public static void sendMessage(Player player, String message, String hoverMessage, String clickCommand) {
		sendMessage(Arrays.asList(player), message, hoverMessage, clickCommand);
	}

	public static void sendMessage(Collection<Player> players, String message, String hoverMessage,
			String clickCommand) {
		sendMessage(players, message, Arrays.asList(hoverMessage), clickCommand);
	}

	public static void sendMessage(Collection<Player> players, String message, List<String> hoverMessages,
			String clickCommand) {
		sendMessage(players, message, hoverMessages, clickCommand, true);
	}

	/**
	 * Envia mensagems clicaveis para varios jogadores
	 * 
	 * @param players
	 * @param message
	 * @param hoverMessages
	 * @param clickCommand
	 * @param runCommand
	 */
	public static void sendMessage(Collection<Player> players, String message, List<String> hoverMessages,
			String clickCommand, boolean runCommand) {

		TextComponent spigotMessage = getText(message, hoverMessages, clickCommand, runCommand);

		for (Player player : players) {
			player.spigot().sendMessage(spigotMessage);
		}

	}

	/**
	 * Envia mensagems clicaveis para varios jogadores
	 * 
	 *
	 * @param message
	 * @param hoverMessages
	 * @param clickCommand
	 * @param runCommand
	 */
	public static TextComponent getText(String message, List<String> hoverMessages, String clickCommand,
			boolean runCommand) {

		// String lastColor = "";

		ComponentBuilder builder = new ComponentBuilder("");

		for (String line : hoverMessages) {
			builder.append(line, FormatRetention.FORMATTING);
			// arrumar aqui
		}

		HoverEvent hoverEvent = new HoverEvent(HoverEvent.Action.SHOW_TEXT, builder.create());
		ClickEvent clickEvent = new ClickEvent(
				runCommand ? ClickEvent.Action.RUN_COMMAND : ClickEvent.Action.SUGGEST_COMMAND, clickCommand);
		TextComponent text = new TextComponent(message);
		text.setHoverEvent(hoverEvent);
		text.setClickEvent(clickEvent);

		return text;
	}

	public static String getLastColor(String text) {
		char[] array = text.toLowerCase().toCharArray();
		String lastColor = "";
		String lastFormat = "";
		char lastChar = 0;
		for (char caracter : array) {
			if (lastChar == ChatColor.COLOR_CHAR) {
				lastColor = "" + ChatColor.COLOR_CHAR + caracter;
			}
			lastChar = caracter;
		}
		return lastColor + lastFormat;
	}

	public static final ChatColor SUCCESS = ChatColor.GREEN;
	public static final ChatColor SUCCESS_ARGUMENT = ChatColor.DARK_GREEN;
	public static final ChatColor ERROR = ChatColor.RED;
	public static final ChatColor ERROR_ARGUMENT = ChatColor.DARK_RED;
	public static final ChatColor MESSAGE = ChatColor.GOLD;
	public static final ChatColor MESSAGE_ARGUMENT = ChatColor.YELLOW;
	public static final ChatColor CHAT_CLEAR = ChatColor.WHITE;
	public static final ChatColor CHAT_NORMAL = ChatColor.GRAY;
	public static final ChatColor GUI_TITLE = ChatColor.BLACK;
	public static final ChatColor GUI_TEXT = ChatColor.DARK_GRAY;
	public static final ChatColor CONFIG = ChatColor.AQUA;
	public static final ChatColor CONFIG_ARGUMENT = ChatColor.DARK_AQUA;
	public static final ChatColor ITEM_TITLE = ChatColor.LIGHT_PURPLE;
	public static final ChatColor ITEM_TEXT = ChatColor.DARK_PURPLE;
	public static final ChatColor TITLE = ChatColor.DARK_BLUE;
	public static final ChatColor TEXT = ChatColor.BLUE;

}
