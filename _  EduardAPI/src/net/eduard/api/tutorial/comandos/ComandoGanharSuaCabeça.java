package net.eduard.api.tutorial.comandos;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class ComandoGanharSuaCabeça implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		Player jogador = (Player) sender;
		if (command.getName().equalsIgnoreCase("headof")) {
			ItemStack cabeça = new ItemStack(Material.SKULL_ITEM,1,(short) 3);
			SkullMeta meta = (SkullMeta) cabeça.getItemMeta();
			meta.setOwner(jogador.getName());
			cabeça.setItemMeta(meta);
			jogador.sendMessage("§aVoce ganhou sua cabeça!");
			jogador.getInventory().addItem(cabeça);
		}
		return true;
	}
}
