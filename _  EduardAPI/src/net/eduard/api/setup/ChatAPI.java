package net.eduard.api.setup;

import org.bukkit.ChatColor;

/**
 * API de manipulação de Cores e CHAT do Minecraft
 * 
 * @author Eduard
 *
 */
public final class ChatAPI {

	public static ChatColor SUCCESS = ChatColor.GREEN;
	public static ChatColor SUCCESS_ARGUMENT = ChatColor.DARK_GREEN;
	public static ChatColor ERROR = ChatColor.RED;
	public static ChatColor ERROR_ARGUMENT = ChatColor.DARK_RED;
	public static ChatColor MESSAGE = ChatColor.GOLD;
	public static ChatColor MESSAGE_ARGUMENT = ChatColor.YELLOW;
	public static ChatColor CHAT_CLEAR = ChatColor.WHITE;
	public static ChatColor CHAT_NORMAL = ChatColor.GRAY;
	public static ChatColor GUI_TITLE = ChatColor.BLACK;
	public static ChatColor GUI_TEXT = ChatColor.DARK_GRAY;
	public static ChatColor CONFIG = ChatColor.AQUA;
	public static ChatColor CONFIG_ARGUMENT = ChatColor.DARK_AQUA;
	public static ChatColor ITEM_TITLE = ChatColor.LIGHT_PURPLE;
	public static ChatColor ITEM_TEXT = ChatColor.DARK_PURPLE;
	public static ChatColor TITLE = ChatColor.DARK_BLUE;
	public static ChatColor TEXT = ChatColor.BLUE;

	/**
	 * Pega um Coração vermelho
	 * 
	 * @return Simbolo Coração
	 */
	public static String getRedHeart() {
		return ChatColor.RED + getHeart();
	}
	 /**
	  *  Pega um Coração normal
	  * @return Simbolo Coração
	  */
	public static String getHeart() {
		return "♥";
	}

	public static String getArrow() {
		return "➵";
	}

	public static String getArrowRight() {
		return "››";
	}

	public static String getArrowLeft() {
		return "‹‹";
	}

	public static String getSquared() {
		return "❑";
	}

	public static String getInterrogation() {
		return "➁";
	}

	public static String getALlSimbols() {
		return "❤❥✔✖✗✘❂⋆✢✣✤✥✦✩✪✫✬✭✵✴✳✲✱★✰✯✮✶✷✸✹✺✻✼❄❅✽✡☆❋❊❉❈❇❆✾✿❀❁❃✌♼♽✂➣➢⬇➟⬆⬅➡✈✄➤➥➦➧➨➚➘➙➛➶➵➴➳➲➸➞➝➜➷➹➹➺➻➼➽Ⓜ⬛⬜ℹ☕▌▄▆▜▀▛█";
	}

	public static String getAllSimbols2() {
		return "™⚑⚐☃⚠⚔⚖⚒⚙⚜⚀⚁⚂⚃⚄⚅⚊⚋⚌⚍⚏⚎☰☱☲☳☴☵☶☷⚆⚇⚈⚉♿♩♪♫♬♭♮♯♠♡♢♗♖♕♔♧♛♦♥♤♣♘♙♚♛♜♝♞♟⚪➃➂➁➀➌➋➊➉➈➇➆➅➄☣☮☯⚫➌➋➊➉➈➇➆➅➄➍➎➏➐➑➒➓ⓐⓑⓚ";
	}

	public static String getAllSimbols3() {
		return "웃유♋♀♂❣¿⌚☑▲☠☢☿Ⓐ✍☤✉☒▼⌘⌛®©✎♒☁☼ツღ¡Σ☭✞℃℉ϟ☂¢£⌨⚛⌇☹☻☺☪½∞✆☎⌥⇧↩←→↑↓⚣⚢⌲♺☟☝☞☜➫❑❒◈◐◑«»‹›×±※⁂‽¶—⁄—–≈÷≠π†‡‡¥€‰●•·";
	}

}
