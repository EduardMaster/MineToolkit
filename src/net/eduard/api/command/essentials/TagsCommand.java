package net.eduard.api.command.essentials;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.game.Tag;
import net.eduard.api.setup.manager.CommandManager;

public class TagsCommand extends CommandManager {
	public List<Tag> tags = new ArrayList<Tag>();
	private static TagsCommand tagsCommand;
	private String text = "§a§l+==== TAGS ====-+";
	public static TagsCommand getTags() {
		return tagsCommand;
	}
	public TagsCommand() {
		super("tags");
		tagsCommand = this;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Mine.chat(sender, text);
		for (Tag tag : tags) {
			if (sender.hasPermission("tag." + tag.getName())) {
				String text = tag.getPrefix() + " " + sender.getName()
						+ tag.getSuffix() + " Mensagem...";
				sender.sendMessage(Mine.toChatMessage(text));
			}
		}

		return true;
	}
}