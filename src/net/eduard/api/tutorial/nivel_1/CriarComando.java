package net.eduard.api.tutorial.nivel_1;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CriarComando implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 0) {
				player.sendMessage("§c/comando usar");
			}else {
				if (args[0].equalsIgnoreCase("usar")) {
					player.sendMessage("§aEnvia uma mensagem Simples!");
				}
			}
			
		}else {
			sender.sendMessage("§cComando apenas para jogadores!");
		}
		return false;
	}
	public static void registrarComando() {
		PluginCommand comando = Bukkit.getPluginCommand("comando");
		comando.setExecutor(new CriarComando());
		comando.setPermission("permissao.usar");
		comando.setPermissionMessage("§cVoce nao tem permissao para usar este comando!");
		comando.setUsage("/comando usar [jogador]");
		comando.setAliases(Arrays.asList("comando2", "cmand"));
		comando.setDescription("§aUma outra descrição para este comando");
		comando.setTabCompleter(new CriarTabCompleter());
	}

	public static void segundaFormaDeRegistrar(JavaPlugin plugin) {
		PluginCommand comando = plugin.getCommand("comando");
		comando.setExecutor(new CriarComando());
		comando.setTabCompleter(new CriarTabCompleter());
	}
}
