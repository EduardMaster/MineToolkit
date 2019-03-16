package net.eduard.api.lib.old;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import net.eduard.api.lib.game.DisplayBoard;
import net.eduard.api.lib.modules.Scoreboards;

/**
 * Sistema de criar scoreboard mais facil
 * 
 * @deprecated Versão atual {@link DisplayBoard} e {@link Scoreboards}<br>
 *             Versão nova {@link net.eduard.api.lib.old.Scoreboard}
 * @version 1.0
 * @author Eduard
 * @see TextSetup
 */
public class ScoreboardSetup implements TextSetup {
	private Scoreboard score;
	private Objective obj;
	private List<ScoreSlotSetup> slots = new ArrayList<>();

	private int sizeName;

	private String name;

	private String colorName;

	private int idName;

	public ScoreboardSetup() {
		this("§6§lSimples ScoreBoard");
	}

	public ScoreboardSetup(String scoreboardName) {
		this.score = Bukkit.getScoreboardManager().getNewScoreboard();
		this.obj = this.score.registerNewObjective(getText(32, scoreboardName), "dummy");
		this.obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		setName(scoreboardName);
		setup();
	}

	public Scoreboard getScoreboard() {
		return this.score;
	}

	private void setup() {
		setNameSize(16, "§f§l");
		new TimeSetup(20, 100000) {
			public void event() {
				String text = ChatColor.stripColor(ScoreboardSetup.this.name);
				if (text.length() > ScoreboardSetup.this.sizeName) {
					text = text + ScoreboardSetup.this.getTextFake(5);
					ScoreboardSetup.this.idName += 1;
					String message = "";
					if (ScoreboardSetup.this.idName > text.length()) {
						ScoreboardSetup.this.idName = 0;
						message = text.substring(0, ScoreboardSetup.this.sizeName);
					} else if (ScoreboardSetup.this.idName > text.length() - ScoreboardSetup.this.sizeName) {
						String frontText = text.substring(0,
								ScoreboardSetup.this.idName + (ScoreboardSetup.this.sizeName - text.length()));
						message = text.substring(0 + ScoreboardSetup.this.idName, text.length()) + frontText;
					} else {
						message = text.substring(0 + ScoreboardSetup.this.idName,
								ScoreboardSetup.this.sizeName + ScoreboardSetup.this.idName);
					}
					ScoreboardSetup.this.obj.setDisplayName(ScoreboardSetup.this.colorName + message);
				}
			}
		}.start();
	}

	public void setNameSize(int nameSize, String colorBeforeName) {
		if (nameSize < 5) {
			nameSize = 5;
		}
		if (nameSize + colorBeforeName.length() > 32) {
			nameSize -= colorBeforeName.length();
		}
		this.sizeName = nameSize;
		this.colorName = colorBeforeName;
	}

	public void setName(String name) {
		this.obj.setDisplayName(getText(32, name));
		this.name = name;
		this.idName = 0;
	}

	public void setEmpty(int size) {
		for (int x = 1; x < 15; x++) {
			removeSlot(x);
		}
		for (int x = 1; x <= size; x++) {
			setTextFake(x);
		}
	}

	public void removeSlot(int id) {
		List<ScoreSlotSetup> list = new ArrayList<>();
		for (ScoreSlotSetup slot : this.slots) {
			if (slot.getSlot() == id) {
				list.add(slot);
			}
		}
		for (ScoreSlotSetup slot : list) {
			this.score.resetScores(Bukkit.getOfflinePlayer(getText(slot.getText())));
			this.slots.remove(slot);
		}
	}

	public void setSlot(ScoreSlotSetup slot) {
		if (slot == null)
			return;
		removeSlot(slot.getSlot());
		this.obj.getScore(Bukkit.getOfflinePlayer(slot.getText())).setScore(slot.getSlot());
		this.slots.add(slot);
	}

	private String getTextFake(int id, int size) {
		if (((size < 1 ? 1 : 0) | (size > 14 ? 1 : 0)) != 0) {
			size = 14;
		}
		return ChatColor.values()[id] + getTextFake(size);
	}

	private String getTextFake(int size) {
		String text = "";
		for (int x = size; x > 1; x--) {
			text = text.concat(" ");
		}
		return text;
	}

	public void setTextFake(int id) {
		setTextFake(id, 10);
	}

	private void setTextFake(int id, int size) {
		setSlot(new ScoreSlotSetup(id, getTextFake(id, size)));
	}

}
