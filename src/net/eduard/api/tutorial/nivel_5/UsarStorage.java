package net.eduard.api.tutorial.nivel_5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.config.Config;
import net.eduard.api.setup.Mine;
import net.eduard.api.setup.StorageAPI;
import net.eduard.api.setup.StorageAPI.Reference;
import net.eduard.api.setup.StorageAPI.Storable;

public class UsarStorage implements Listener {
	@EventHandler
	public void cmd(ServerCommandEvent e) {
		if (e.getCommand().startsWith("re")) {
			reload();
			e.setCancelled(true);
			e.getSender().sendMessage("§aRecarregar");
		}
		if (e.getCommand().startsWith("sa")) {
			save();
			e.getSender().sendMessage("§aSalvar");
			e.setCancelled(true);
		}
	}

	public static JavaPlugin plugin;
	public static Config config;
	public static void reload() {
		
			config.reloadConfig();
		if (config.contains("lugar")) {
			manager = (ContaManager) config.get("lugar");
			int id = StorageAPI.getIdByObject(manager);
			System.out.println("Antes Id "+id);
			StorageAPI.updateReferences();
			id = StorageAPI.getIdByObject(manager);
			System.out.println("Depois Id "+id);
		} else {
			manager = new ContaManager();
			Conta conta = new Conta();
			conta.dinheiro = 5;
			conta.nome = "Eduard";
			manager.contas.add(conta);
			manager.objetos.add(conta);
				save();

		}
	}

	public static void save() {
		config.set("lugar", manager);
		config.saveConfig();
		StorageAPI.updateReferences();
	}

	public static ContaManager manager;

	public static void register(JavaPlugin plugin) {
		UsarStorage.plugin = plugin;
		UsarStorage evento = new UsarStorage();
		Mine.registerEvents(evento, plugin);
		StorageAPI.register(ContaManager.class);
		StorageAPI.register(Conta.class);
		config = new Config(plugin, "storage.yml");
		reload();

	}

	public static class Conta implements Storable {
		private String nome;
		private int dinheiro;

		public int getDinheiro() {
			return dinheiro;
		}

		public void setDinheiro(int dinheiro) {
			this.dinheiro = dinheiro;
		}

		public String getNome() {
			return nome;
		}

		public void setNome(String nome) {
			this.nome = nome;
		}

		@Override
		public Object restore(Map<String, Object> map) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void store(Map<String, Object> map, Object object) {
			// TODO Auto-generated method stub

		}
	}

	public static class ContaManager implements Storable {

		private List<Conta> objetos = new ArrayList<>();
		@Reference
		private List<Conta> contas = new ArrayList<>();

		@Override
		public Object restore(Map<String, Object> map) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void store(Map<String, Object> map, Object object) {
			// TODO Auto-generated method stub

		}

	}

}
