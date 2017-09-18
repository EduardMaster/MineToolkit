package net.eduard.api.tutorial.comandos;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ComandoBuild implements CommandExecutor, Listener {

	public static List<Player> listasDosJogadoresQueEstamUsandoBuild = new ArrayList<>();
	public static String ativarBuild = "§6Voce ativou o BUILD";
	public static String destivarBuild = "§6Voce desativou o BUILD";

	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (sender instanceof Player) {
			Player jogador = (Player) sender;
			// Se o jogador estiver na lista o remove da lista e manda mensagem
			// Caso contrario adiciona na lista e manda outra mensagem
			
			if (listasDosJogadoresQueEstamUsandoBuild.contains(jogador)) {
				listasDosJogadoresQueEstamUsandoBuild.remove(jogador);
				jogador.sendMessage(destivarBuild);
			} else {
				listasDosJogadoresQueEstamUsandoBuild.add(jogador);
				jogador.sendMessage(ativarBuild);
			}
		}
		return false;
	}
	private JavaPlugin plugin;
	public ComandoBuild(JavaPlugin plugin) {
		this.plugin = plugin;
		onEnable();
	}
	public void onEnable() {
		Bukkit.getPluginCommand("build").setExecutor(this);
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void quandoOjogadorQuebrarUmBloco(BlockBreakEvent e) {
		Player jogador = e.getPlayer();
		// Cancela o bloco quebrado se nao tiver o jogador na lista
		e.setCancelled(!listasDosJogadoresQueEstamUsandoBuild.contains(jogador));
	}
	@EventHandler
	public void quandoOjogadorColocarUmBloco(BlockPlaceEvent e) {
		Player jogador = e.getPlayer();
		// Cancela o bloco colocado se nao tiver o jogador na lista
		e.setCancelled(!listasDosJogadoresQueEstamUsandoBuild.contains(jogador));
	}

}
