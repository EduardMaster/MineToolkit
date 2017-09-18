package net.eduard.api.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.eduard.api.API;
import net.eduard.api.game.Tag;
import net.eduard.api.manager.CMD;
import net.eduard.api.setup.ExtraAPI;

public class TagsCommand extends CMD {
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
		API.chat(sender, text);
		for (Tag tag : tags) {
			if (sender.hasPermission("tag." + tag.getName())) {
				String text = tag.getPrefix() + " " + sender.getName()
						+ tag.getSuffix() + " Mensagem...";
				sender.sendMessage(ExtraAPI.toChatMessage(text));
			}
		}

		return true;
	}
}