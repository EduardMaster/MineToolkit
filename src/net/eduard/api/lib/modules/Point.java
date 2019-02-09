package net.eduard.api.lib.modules;

import org.bukkit.ChatColor;

/**
 * Ponto de direção usado para fazer um RADAR
 * 
 * 
 *
 */
public  enum Point {
	N('N'), NE('/'), E('O'), SE('\\'), S('S'), SW('/'), W('L'), NW('\\');

	public final char asciiChar;

	private Point(char asciiChar) {
		this.asciiChar = asciiChar;
	}

	@Override
	public String toString() {
		return String.valueOf(this.asciiChar);
	}

	public String toString(boolean isActive, ChatColor colorActive, String colorDefault) {
		return (isActive ? colorActive : colorDefault) + String.valueOf(this.asciiChar);
	}

	public String toString(boolean isActive, String colorActive, String colorDefault) {
		return (isActive ? colorActive : colorDefault) + String.valueOf(this.asciiChar);
	}
}