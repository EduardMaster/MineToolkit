
package net.eduard.api.command;

import net.eduard.api.lib.modules.MineReflect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.manager.CommandManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class FakeCommand extends CommandManager {

	public static class Fake {



		public Fake(Player player) {
			setPlayer(player);
			setOriginal(player.getName());
			setFake(player.getName());
		}


		private Player player;

		private String fake;

		private String original;

		public String getOriginal() {
			return original;
		}

		public void setOriginal(String original) {
			this.original = original;
		}

		public Player getPlayer() {
			return player;
		}

		public void setPlayer(Player player) {
			this.player = player;
		}

		public String getFake() {
			return fake;
		}

		public void setFake(String fake) {
			this.fake = fake;
		}



	}

	private static Map<Player, Fake> fakes = new HashMap<>();
	public static boolean canFake(String name) {
		for (Fake fake : fakes.values()) {
			if (fake.getFake().equalsIgnoreCase(name) || fake.getOriginal().equalsIgnoreCase(name)) {
				return false;
			}

		}
		return true;
	}

	public static void removeFake(String name) {
		for (Fake fake : fakes.values()) {
			if (fake.getFake().equalsIgnoreCase(name)) {
				reset(fake.getPlayer());
			}
		}
	}

	public static Fake getFake(Player player) {
		Fake fake = fakes.get(player);
		if (fake == null) {
			fake = new Fake(player);
			fakes.put(player, fake);
		}
		return fake;
	}
	public static Map<Player, Fake> getFakes() {
		return fakes;
	}

	public static void fake(Player player, String name) {
		Fake fake = getFake(player);
		fake.setFake(name);
		player.setDisplayName(name);
		player.setPlayerListName(name);
		player.setCustomName(name);
		player.setCustomNameVisible(true);
//		for (Player p : Mine.getPlayers()) {
//			if (p == player)continue;
//			p.hidePlayer(player);
//		}
//		for (Player p : Mine.getPlayers()) {
//			if (p == player)continue;
//			p.showPlayer(player);
//		}
		MineReflect.changeName(player, name);
	}

	public static void reset(Player player) {
		Fake fake =  getFake(player);
		String name = fake.getOriginal();
		fake.setFake(name);

		MineReflect.changeName(player, name );
		player.setDisplayName(name);
		player.setPlayerListName(name);
	}


	@EventHandler
	public void event(PlayerMoveEvent e) {
	}
	@EventHandler
	public void event(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		removeFake(p.getName());
//		FakeAPI.getFakes().remove(p);
	}
	@EventHandler
	public void event(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		String name = p.getName();
		removeFake(name);
		getFake(p);
	}
	public FakeCommand() {
		super("fake");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
		String[] args) {
		if (Mine.onlyPlayer(sender)) {
			Player p = (Player)sender;
			if (args.length == 0) {
				p.sendMessage("§c/fake <jogador> §7Ativar um fake de um jogador");
				p.sendMessage("§c/fake resetar §7Resetar o fake");
			}else {
				String cmd = args[0];
				if (cmd.equalsIgnoreCase("reset")|cmd.equalsIgnoreCase("resetar")) {
					reset(p);
					p.sendMessage("§aFake resetado!");
				}else {
					String nome = cmd;
					fake(p, nome);
					p.sendMessage("§aFake ativado do jogador "+nome);
					
				}
			}
		}
		
		return true;
	}

}
