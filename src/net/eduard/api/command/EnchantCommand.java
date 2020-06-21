
package net.eduard.api.command;

import java.util.ArrayList;
import java.util.List;

import net.eduard.api.lib.modules.Extra;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.manager.CommandManager;

public class EnchantCommand extends CommandManager {

	public EnchantCommand() {
		super("enchantment");
 
	}
	public String messageInvalid = "§cDigite o encantamento valido! §bAperte TAB";
	public String message = "§aEncantamento aplicado!";
	public String messageError = "§cVoce precisar ter um item em maos!";

	public static List<String> getEnchants(String argument) {
		if (argument == null) {
			argument = "";
		}
		argument = argument.trim().replace("_", "");
		List<String> list = new ArrayList<>();

		for (Enchantment enchant : Enchantment.values()) {
			String text = Extra.toTitle(enchant.getName(), "");
			String line = enchant.getName().trim().replace("_", "");
			if (Extra.startWith(line, argument)) {
				list.add(text);
			}
		}
		return list;

	}
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 1) {
			return EnchantCommand.getEnchants(args[0]);
		}

		return null;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 0) {
				return false;
			} else {
				if (p.getItemInHand() == null
						|| p.getItemInHand().getType() == Material.AIR) {
					p.sendMessage(messageError);
					return true;
				}
				Enchantment enchant = Mine.getEnchant(args[0]);
				if (enchant == null) {
					p.sendMessage(messageInvalid);
				} else {
					int nivel = 1;
					if (args.length >= 2) {
						nivel = Extra.toInt(args[1]);
					}
					if (nivel == 0) {
						p.getItemInHand().removeEnchantment(enchant);
					} else
						p.getItemInHand().addUnsafeEnchantment(enchant, nivel);
					p.sendMessage(message);
				}
			}
		}
		return true;
	}

}
