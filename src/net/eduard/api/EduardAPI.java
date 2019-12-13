package net.eduard.api;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import net.eduard.api.command.*;
import net.eduard.api.lib.modules.EnchantGlow;
import net.eduard.api.manager.PlayerSkin;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import net.eduard.api.command.api.ApiCommand;
import net.eduard.api.command.map.MapCommand;
import net.eduard.api.lib.Mine;
import net.eduard.api.lib.bungee.BukkitController;
import net.eduard.api.lib.bungee.BungeeAPI;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.event.PlayerTargetEvent;
import net.eduard.api.lib.game.DisplayBoard;
import net.eduard.api.lib.game.SoundEffect;
import net.eduard.api.lib.game.Tag;
import net.eduard.api.lib.manager.CommandManager;
import net.eduard.api.lib.manager.DBManager;
import net.eduard.api.lib.manager.EventsManager;
import net.eduard.api.lib.manager.TimeManager;
import net.eduard.api.lib.menu.Menu;
import net.eduard.api.lib.menu.Shop;
import net.eduard.api.lib.modules.BukkitBungeeAPI;
import net.eduard.api.lib.modules.Copyable.CopyDebug;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.modules.ServerAPI.BukkitControl;
import net.eduard.api.lib.modules.VaultAPI;
import net.eduard.api.lib.storage.StorageAPI;
import net.eduard.api.lib.storage.bukkit_storables.BukkitStorables;
import net.eduard.api.manager.BukkitReplacers;
import net.eduard.api.manager.EssentialsEvents;
import net.eduard.api.manager.InfoGenerator;
import net.eduard.api.server.EduardPlugin;
import net.eduard.api.server.minigame.Minigame;

/**
 * Classe Principal do EduardAPI
 * 
 * @author Eduard
 * @version 1.2
 * @since 0.5
 * 
 * 
 */
public class EduardAPI extends EduardPlugin {

	private static EduardAPI plugin;
	/**
	 * Som do rosnar do gato
	 */
	@SuppressWarnings("unused")
	private static final SoundEffect ROSNAR = SoundEffect.create("CAT_PURR");

	public static EduardAPI getInstance() {
		return plugin;
	}

	public Config getMessages() {
		return messages;
	}

	public Config getConfigs() {
		return config;
	}

	public void onLoad() {
		
		plugin = this;
		setFree(true);
		initConfig();
		StorageAPI.setDebug(config.getBoolean("debug-storage"));
		log("Registrando classes da EduardLIB");
		StorageAPI.registerPackage(getClass(), "net.eduard.api.lib");
		BukkitStorables.load();
		super.onLoad();
		log("Storables do Bukkit carregado!");
	}
	public void onEnable() {
		
		VaultAPI.setupVault();
		
		BukkitControl.register(this);
		BukkitBungeeAPI.requestCurrentServer();
		BukkitController bukkit = BungeeAPI.getBukkit();
		
		bukkit.setPlugin(plugin);
		bukkit.register();
		EnchantGlow.getGlow();
		StorageAPI.setDebug(config.getBoolean("debug-storage"));
		StorageAPI.registerPackage(Minigame.class);

	
		
		
		log("Inicio do Recarregamento do EduardAPI");
		reload();
	
		Mine.resetScoreboards();
		log("Scoreboards resetadas!");
		asyncTimer(new Runnable() {

			@Override
			public void run() {

				for (Player p : Mine.getPlayers()) {

					PlayerTargetEvent event = new PlayerTargetEvent(p,
							Mine.getTarget(p, Mine.getPlayerAtRange(p.getLocation(), 100)));
					Mine.callEvent(event);

				}

			}
		}, 20, 20);
		asyncTimer(new Runnable() {

			@Override
			public void run() {

				for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
					if (plugin instanceof EduardPlugin) {
						EduardPlugin pl = (EduardPlugin) plugin;
						long agora = Extra.getNow();
						if (pl.isAutoSaving()) {
							if (pl.getAutoSaveLastTime() + pl.getAutoSaveSeconds() * 1000 < agora) {
								log("Salvando dados do plugin §b" + pl.getName());
								long tempo1 = Extra.getNow();
								pl.autosave();
								long tempo2 = Extra.getNow();
								log("Tempo levado para salvar os dados do plugin: §a" + (tempo2 - tempo1)
										+ " milisegundos");
								log("§7-----");
							}
						}
						if (pl.isBackup()) {
							if (pl.getBackupLastTime()
									+ pl.getBackupTimeUnitType().toMillis(pl.getBackupTime()) < agora) {
								log("Iniciando sistema de backup para o plugin §b"+ pl.getName());
								log("Deletando ultimos backups");
								long tempo1 = Extra.getNow();
								pl.deleteLastBackups();
								long tempo2 = Extra.getNow();
								log("Tempo levado para deletar os backups: §a" + (tempo2 - tempo1) + " milisegundos");
								log("Fazendo backup ");
								long tempo3 = Extra.getNow();
								pl.backup();
								long tempo4 = Extra.getNow();
								log("Backup finalizado tempo levado para fazer: §a" + (tempo4 - tempo3)
										+ " milisegundos");
								log("§7-----");
							}
						}

					}
				}

			}
		}, 20, 20);
		log("Ativando comandos da API");
		new ApiCommand().register();
		new MapCommand().register();
		new EnchantCommand().register();
		new GotoCommand().register();
		new SoundCommand().register();
		new SetXPCommand().register();
		new SetSkinCommand().register();


		new EssentialsEvents().register(this);
		log("Comandos ativado com sucesso");
		
		new InfoGenerator(this);
		log("Gerando Base de dados em forma de texto nos arquivos yml!");

		
		log("Definindo valores das classes da API");
		Extra.setPrice(EduardPlugin.class, 4);
		Extra.setPrice(Menu.class, 4);
		Extra.setPrice(Shop.class, 1);
		Extra.setPrice(CommandManager.class, 0);
		Extra.setPrice(TimeManager.class, 0);
		Extra.setPrice(EventsManager.class, 1);
		Extra.setPrice(DBManager.class, 15);
		Extra.setPrice(Tag.class, 1);
		Extra.setPrice(PluginMessageListener.class, 10);
		Extra.setPrice(DisplayBoard.class, 5);

		log("Ativando replacers");
		new BukkitReplacers();
		
//		new McMMOReplacers();
//		new MassiveFactionReplacers();
		
		log("Carregado com sucesso!");
		

	}
	public void reload() {
		log("Aplicando debugs se estiverem ativados na config");
		StorageAPI.setDebug(config.getBoolean("debug-storage"));
		DBManager.setDebug(config.getBoolean("debug-db"));
		Menu.setDebug(config.getBoolean("debug-menu"));
		CommandManager.setDebug(config.getBoolean("debug-commands"));
		CopyDebug.setDebug(config.getBoolean("debug-copyable"));
		BukkitBungeeAPI.setDebug(config.getBoolean("debug-bungee-bukkit"));
		Mine.OPT_DEBUG_REPLACERS = config.getBoolean("debug-replacers");
		PlayerSkin.reloadSkins();

		
		Mine.loadMaps();
		log("Mapas carregados!");

		config.add("sound-teleport", Mine.OPT_SOUND_TELEPORT);
		config.add("sound-error", Mine.OPT_SOUND_ERROR);
		config.add("sound-success", Mine.OPT_SOUND_SUCCESS);
		config.saveConfig();
		Mine.OPT_AUTO_RESPAWN = config.getBoolean("auto-respawn");
		Mine.OPT_NO_JOIN_MESSAGE = config.getBoolean("no-join-message");
		Mine.OPT_NO_QUIT_MESSAGE = config.getBoolean("no-quit-message");
		Mine.OPT_NO_DEATH_MESSAGE = config.getBoolean("no-death-message");
		try {
			log("Carregando formato de dinheiro da config");
			Extra.MONEY = new DecimalFormat(config.getString("money-format"),
					DecimalFormatSymbols.getInstance(Locale.forLanguageTag(config.getString("money-format-locale"))));
			log("Formato valido");
		} catch (Exception e) {
			error("Formato do dinheiro invalido " + config.getString("money-format"));
		}

		Mine.MSG_ON_JOIN = config.message("on-join-message");
		Mine.MSG_ON_QUIT = config.message("on-quit-message");
		Mine.OPT_SOUND_TELEPORT = config.getSound("sound-teleport");
		Mine.OPT_SOUND_ERROR = config.getSound("sound-error");
		Mine.OPT_SOUND_SUCCESS = config.getSound("sound-success");
		if (config.getBoolean("auto-rejoin")) {
			for (Player p : Mine.getPlayers()) {
				Mine.callEvent(new PlayerJoinEvent(p, null));
			}
		}

	}

	@Override
	public void onDisable() {
		PlayerSkin.saveSkins();
		Mine.saveMaps();
		log("Mapas salvados!");
		log("desativado com sucesso!");
		BungeeAPI.getController().unregister();
	}

}
