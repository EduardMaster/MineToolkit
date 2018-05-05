package net.eduard.api.setup.old;

import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
/**
 * API para utilização de metodos do Spigot com mais facilidade
 * @author Eduard
 * @version 1.0
 * @since Lib v1.0
 *
 */
@Deprecated
public final class SpigotAPI {
	/**
	 * Envia uma mensagem clicavel para o Jogador
	 * @param player Jogador
	 * @param clickMessage Mensagem
	 * @param hoverMessage Mensagem ao passar o Mouse
	 * @param command Comando executado ao clicar
	 */
	public static void sendClickable(Player player, String clickMessage,
			String hoverMessage, String command) {
		player.spigot().sendMessage(getClickable(clickMessage, hoverMessage, command));
	}
	/**
	 * Cria um Componente
	 * @param message Mensagem
	 * @param hoverMessage Mensagem ao passar o Mouse
	 * @param command Comando executado ao clicar
	 * @return Componente
	 */
	public static TextComponent getClickable(String message, String hoverMessage,
			String command) {
		TextComponent text = new TextComponent(message);
		text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(hoverMessage).create()));
		text.setClickEvent(new ClickEvent(Action.RUN_COMMAND, command));
		return text;
	}
}
