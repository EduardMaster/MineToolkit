package net.eduard.api.setup;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.protocol.packet.Chat;
import net.md_5.bungee.api.chat.ClickEvent.Action;
/**
 * API para utilização de metodos do Spigot com mais facilidade
 * 
 * @author Eduard-PC
 *
 */
public final class SpigotAPI {
	/**
	 * Envia uma mensagem clicavel para o Jogador
	 * 
	 * @param player
	 *            Jogador
	 * @param clickMessage
	 *            Mensagem
	 * @param hoverMessage
	 *            Mensagem ao passar o Mouse
	 * @param command
	 *            Comando executado ao clicar
	 */
	public static void sendClickable(Player player, String clickMessage,
			String hoverMessage, String command) {
		player.spigot()
				.sendMessage(getClickable(clickMessage, hoverMessage, command));
	}
	/**
	 * Cria um Componente
	 * 
	 * @param message
	 *            Mensagem
	 * @param hoverMessage
	 *            Mensagem ao passar o Mouse
	 * @param command
	 *            Comando executado ao clicar
	 * @return Componente
	 */
	public static TextComponent getClickable(String message,
			String hoverMessage, String command) {
		TextComponent text = new TextComponent(message);
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(hoverMessage).create()));
		text.setClickEvent(new ClickEvent(Action.RUN_COMMAND, command));
		return text;
	}
	public static TextComponent getTextCorrect(String text) {
		String test = text;
		String lastColor = "";
		TextComponent result = new TextComponent("");
		while (test.length() > 55) {
			int var = 55 - lastColor.length();
			String line = test.substring(0, var);
			result.addExtra(lastColor + line);

			String color = ChatColor.getLastColors(line);
			test = test.substring(var);
			if (!color.isEmpty()) {
				lastColor = color;
			}
		}
		result.addExtra(lastColor + test);
		return result;
	}
}
