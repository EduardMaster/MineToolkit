package net.eduard.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.game.Tag;
import net.eduard.api.manager.CMD;

public class TagCommand extends CMD
{
	public TagCommand() {
		super("tag");
	}
  @Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    if (API.onlyPlayer(sender)) {
      Player p = (Player)sender;
      if (args.length == 0)
        return false;
      for (Tag tag : TagsCommand.getTags().tags) {
        if (tag.getName().equalsIgnoreCase(args[0])) {
          if (p.hasPermission("Tag." + tag.getName())) {
           	API.setTag(p, tag);
            p.sendMessage(
              "§6Sua tag foi alterada para " + tag.getName());
            return true;
          }
          p.sendMessage("§cVoce não tem permissão para usar este Tag " + 
            args[0]);
        }
      }

      p.sendMessage("§cEsta tag não existe " + args[0]);
    }

    return true;
  }
}