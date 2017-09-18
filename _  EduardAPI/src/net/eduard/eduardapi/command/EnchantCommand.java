
package net.eduard.eduardapi.command;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import net.eduard.api.manager.CMD;
import net.eduard.api.setup.ExtraAPI;

public class EnchantCommand extends CMD {

	public EnchantCommand() {
		super("enchant");

	}
	public String messageInvalid = "§cDigite o encantamento valido! §bAperte TAB";
	public String message = "§aEncantamento aplicado!";
	public String messageError = "§cVocê precisar ter um item em maos!";
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
			String label, String[] args) {
		if (args.length == 1) {
			return ExtraAPI.getEnchants(args[0]);
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
				Enchantment enchant = ExtraAPI.getEnchant(args[0]);
				if (enchant == null) {
					p.sendMessage(messageInvalid);
				} else {
					int nivel = 1;
					if (args.length >= 2) {
						try {
							nivel = Integer.parseInt(args[1]);
						} catch (Exception e) {
						}
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
