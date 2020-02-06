package net.eduard.api.command;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.manager.CommandManager;
import net.eduard.api.manager.PlayerSkin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSkinCommand extends CommandManager {
    public SetSkinCommand() {
        super("setskin");

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length ==0){
            sendUsage(sender);
        }else{
            if (Mine.onlyPlayer(sender)) {
                Player p = (Player) sender;
                String playerName = args[0];
                sender.sendMessage("§aSua skin foi alterada para " + playerName);
                PlayerSkin.change(p, playerName);
            }
        }
        return true;
    }
}
