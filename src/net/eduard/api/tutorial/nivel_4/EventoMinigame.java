package net.eduard.api.tutorial.nivel_4;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class EventoMinigame extends BukkitRunnable implements Listener {

	public enum EventoEstado {
		INICIANDO, JOGANDO, FINALIZADO;
	}

	private static EventoMinigame evento = new EventoMinigame();
	
	static {
		evento.ligarEventos();
		evento.iniciarContagem();
	}

	public static EventoMinigame getEvento() {
		return evento;
	}

	public static void init() {
		
	}

	public static void setEvento(EventoMinigame evento) {
		EventoMinigame.evento = evento;
	}

	private String nome;
	private BukkitTask contagem;
	private EventoEstado estado;
	private int minimoJogadores = 2;

	private Location spawn,lobby;

	private List<Player> jogadores = new ArrayList<>();

	private int tempo;

	public void contagem() {

		if (estado == EventoEstado.INICIANDO) {
			tempo--;
			// if (jogadores.size() <= minimoJogadores) {
			// reiniciarContagem();
			// }
			if (tempo == 0) {
				if (jogadores.size() <= minimoJogadores) {
					finalizar();
				} else {
					iniciarEvento();
				}
			}else {
				Bukkit.broadcastMessage("");
			}

		} else if (estado == EventoEstado.JOGANDO) {

		}

	}

	public void desligarContagem() {
		contagem.cancel();
	}

	public void desligarEventos() {
		HandlerList.unregisterAll(evento);
	}

	public boolean estaJogando(Player player) {
		return jogadores.contains(player);
	}

	public void finalizar() {
		estado = EventoEstado.FINALIZADO;
		tempo = 0;
	}

	public BukkitTask getContagem() {
		return contagem;
	}

	public EventoEstado getEstado() {
		return estado;
	}

	public List<Player> getJogadores() {
		return jogadores;
	}

	public Location getLobby() {
		return lobby;
	}

	public int getMinimoJogadores() {
		return minimoJogadores;
	}

	public String getNome() {
		return nome;
	}

	public Location getSpawn() {
		return spawn;
	}

	public int getTempo() {
		return tempo;
	}

	public void iniciarContagem() {
		if (temContagem()) {
			desligarContagem();
		}
		contagem = this.runTaskTimer(JavaPlugin.getProvidingPlugin(getClass()), 20, 20);
	}
	public void iniciarEvento() {
		estado = EventoEstado.JOGANDO;
		tempo = 0;
	}

	public void jogar(Player player) {
		jogadores.add(player);
	}

	public void ligarEventos() {
		Bukkit.getPluginManager().registerEvents(this, JavaPlugin.getProvidingPlugin(getClass()));
	}

	public void reiniciarContagem() {
		tempo = 60;
	}

	@Override
	public void run() {
		contagem();
	}

	public void sair(Player player) {
		jogadores.remove(player);
	}

	public void setContagem(BukkitTask contagem) {
		this.contagem = contagem;
	}

	public void setEstado(EventoEstado estado) {
		this.estado = estado;
	}

	public void setJogadores(List<Player> jogadores) {
		this.jogadores = jogadores;
	}

	public void setLobby(Location lobby) {
		this.lobby = lobby;
	}

	public void setMinimoJogadores(int minimoJogadores) {
		this.minimoJogadores = minimoJogadores;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	public boolean temContagem() {
		return contagem != null;
	}
	
}
