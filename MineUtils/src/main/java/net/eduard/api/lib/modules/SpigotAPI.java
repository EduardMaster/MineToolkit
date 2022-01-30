package net.eduard.api.lib.modules;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Aliases para utilização de metodos do Spigot com mais facilidade
 *
 * @author Eduard
 * @version 1.3
 * @since EduardAPI v1.0
 */
@SuppressWarnings("unused")
public final class SpigotAPI {
    /**
     * @param player       Jogador
     * @param message      Mensagem
     * @param hoverMessage Mensagem ao passar o Mouse
     * @param textToSend   Texto para enviar
     */
    public static void sendMessage(Player player, String message, String hoverMessage, String textToSend, boolean runCommand) {
        sendMessage(Collections.singletonList(player), message, Collections.singletonList(hoverMessage), textToSend, runCommand);
    }

    /**
     * @param player       Jogador
     * @param message      Mensagem
     * @param hoverMessage Mensagem ao passar o Mouse
     * @param clickCommand Texto para enviar
     */
    public static void sendMessage(Player player, String message, String hoverMessage, String clickCommand) {
        sendMessage(Collections.singletonList(player), message, hoverMessage, clickCommand);
    }

    public static void sendMessage(Collection<Player> players, String message, String hoverMessage, String clickCommand) {
        sendMessage(players, message, Collections.singletonList(hoverMessage), clickCommand);
    }

    public static void sendMessage(Collection<Player> players, String message, List<String> hoverMessages, String clickCommand) {
        sendMessage(players, message, hoverMessages, clickCommand, true);
    }

    /**
     * Envia mensagems clicaveis para varios jogadores
     *
     * @param players       Jogadores
     * @param message       Mensagem
     * @param hoverMessages Mensagem ao passar o Mouse
     * @param clickCommand  Comando para rodar
     * @param runCommand    Se é para rodar comando ou não
     */
    public static void sendMessage(Collection<Player> players, String message, List<String> hoverMessages, String clickCommand, boolean runCommand) {
        TextComponent spigotMessage = getText(message, hoverMessages, clickCommand, runCommand);
        for (Player player : players) {
            player.spigot().sendMessage(spigotMessage);
        }
    }

    /**
     * Envia mensagems clicaveis para varios jogadores
     *
     * @param message       Mensagem
     * @param hoverMessages Mensagem ao passar o Mouse
     * @param clickCommand  Comando para rodar
     * @param runCommand    Se é para rodar comando ou não
     */
    public static TextComponent getText(String message, List<String> hoverMessages, String clickCommand, boolean runCommand) {
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

    public static final ChatColor POSITIVE_ACTION = ChatColor.GREEN;
    public static final ChatColor POSITIVE_ACTION_ARGUMENT = ChatColor.DARK_GREEN;
    public static final ChatColor NEGATIVE_ACTION = ChatColor.RED;
    public static final ChatColor NEGATIVE_ACTION_ARGUMENT = ChatColor.GRAY;
    public static final ChatColor CONFIG = ChatColor.AQUA;
    public static final ChatColor CONFIG_ARGUMENT = ChatColor.DARK_AQUA;
    public static final ChatColor GAME = ChatColor.GOLD;
    public static final ChatColor GAME_ARGUMENT = ChatColor.YELLOW;
    public static final ChatColor CHAT = ChatColor.WHITE;
    public static final ChatColor CHAT_ARGUMENT = ChatColor.GRAY;
    public static final ChatColor TITLE1 = ChatColor.BLACK;
    public static final ChatColor TITLE2 = ChatColor.DARK_RED;
    public static final ChatColor TITLE3 = ChatColor.DARK_PURPLE;
    public static final ChatColor TITLE4 = ChatColor.DARK_BLUE;
    public static final ChatColor TEXT = ChatColor.LIGHT_PURPLE;
    public static final ChatColor TEXT2 = ChatColor.BLUE;
    public static final ChatColor TEXT3 = ChatColor.GRAY;
    public static final ChatColor TEXT4 = ChatColor.DARK_GRAY;
}
