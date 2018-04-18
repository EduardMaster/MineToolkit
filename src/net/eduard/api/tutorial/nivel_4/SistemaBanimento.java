package net.eduard.api.tutorial.nivel_4;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

import net.md_5.bungee.event.EventHandler;

public class SistemaBanimento implements CommandExecutor, Listener {
	public static class Banimento {

		public static Map<String, Banimento> banimentos = new HashMap<>();

		public static void banir(String nome, String autor, String motivo, UUID autorID, UUID banidoId,
				int diasBanido) {
			Banimento ban = new Banimento();
			ban.setAlvo(nome);
			ban.setAlvoId(banidoId);
			ban.setAutor(autor);
			ban.setAutorID(banidoId);
			ban.setDia(Calendar.getInstance().getTime());
			ban.setDuracaoEmDias(diasBanido);
			ban.setMotivo(motivo);
			banimentos.put(nome, ban);
		}

		public static boolean estaBanido(String nome) {
			return banimentos.containsKey(nome);
		}

		public static void desbanir(String name) {
			banimentos.remove(name);
		}

		private String alvo;
		private UUID alvoId;
		private String autor;
		private UUID autorID;
		private String motivo;
		private Date dia;
		private int duracaoEmDias = 1;

		public String getAlvo() {
			return alvo;
		}

		public void setAlvo(String alvo) {
			this.alvo = alvo;
		}

		public UUID getAlvoId() {
			return alvoId;
		}

		public void setAlvoId(UUID alvoId) {
			this.alvoId = alvoId;
		}

		public String getAutor() {
			return autor;
		}

		public void setAutor(String autor) {
			this.autor = autor;
		}

		public UUID getAutorID() {
			return autorID;
		}

		public void setAutorID(UUID autorID) {
			this.autorID = autorID;
		}

		public String getMotivo() {
			return motivo;
		}

		public void setMotivo(String motivo) {
			this.motivo = motivo;
		}

		public Date getDia() {
			return dia;
		}

		public void setDia(Date dia) {
			this.dia = dia;
		}

		public int getDuracaoEmDias() {
			return duracaoEmDias;
		}

		public void setDuracaoEmDias(int duracaoEmDias) {
			this.duracaoEmDias = duracaoEmDias;
		}

	}

	@EventHandler
	public void evento(PlayerKickEvent e) {
		Player p = e.getPlayer();
		if (Banimento.estaBanido(p.getName())) {
			Banimento ban = Banimento.banimentos.get(p.getName());
			e.setLeaveMessage("§cVoce foi banido por " + ban.getAlvo());

		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (args.length <= 1) {
			sender.sendMessage("§c/ban <jogador> <motivo> [dias]");
		} else {
			String alvoNome = args[0];
			int diasBanido = 1;
			if (args.length >= 3) {
				diasBanido = Integer.valueOf(args[2]);
			}
			OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(alvoNome);
			OfflinePlayer autorOffline = Bukkit.getOfflinePlayer(sender.getName());
			String motivo = args[1];
			if (Banimento.estaBanido(alvoNome)) {
				sender.sendMessage("§cJa foi banido!");
			} else {

				Banimento.banir(alvoNome, sender.getName(), motivo, autorOffline.getUniqueId(),
						offlinePlayer.getUniqueId(), diasBanido);
				if (offlinePlayer.isOnline()) {
					offlinePlayer.getPlayer().kickPlayer("§cVocê foi banido!");
				}
			}

		}

		return true;
	}

}
