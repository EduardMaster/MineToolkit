package net.eduard.api.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.modules.BukkitTimeHandler;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.StorageAPI;

/**
 * Representa os plugins feitos pelo Eduard
 * 
 * @version 1.0
 * @since 2.0
 * @author Eduard
 *
 */
public abstract class EduardPlugin extends JavaPlugin implements BukkitTimeHandler {
	private static final SimpleDateFormat DATE_TIME_FORMATER = new SimpleDateFormat("dd-MM-YYYY hh-mm-ss");
	private boolean logEnabled = true;
	private boolean errorLogEnabled = true;
	protected Config config;
	protected Config messages;
	protected Config storage;
	private File databaseFile;
	private boolean free;

	public String getString(String path) {
		return config.message(path);
	}
/**
 * Config padrão do spigot não funciona
 */
	public FileConfiguration getConfig() {
		return null;
	}

	/**
	 * Envia mensagem para o console caso as Log Normais esteja ativada para ele
	 * 
	 * @param msg Mensagem
	 */
	public void log(String msg) {
		if (logEnabled)
			Bukkit.getConsoleSender().sendMessage("§b[" + getName() + "] §f" + msg);
	}

	/**
	 * Envia mensagem para o console caso as Log de Erros esteja ativada para ele
	 * 
	 * @param msg Mensagem
	 */
	public void error(String msg) {
		if (errorLogEnabled)
			Bukkit.getConsoleSender().sendMessage("§b[" + getName() + "] §c" + msg);
	}

	/**
	 * Verifica se tem algo dentro da config.yml
	 * 
	 * @return Se a a config.yml tem configurações
	 */
	public boolean isEditable() {
		return !config.getKeys().isEmpty();
	}

	/**
	 * Verifica se tem mensagens dentro da messages.yml
	 * 
	 * @return Se tem mensagens na messages.yml
	 */
	public boolean hasMessages() {
		return !messages.getKeys().isEmpty();
	}

	/**
	 * Verifica se dados armazenados dentro da storage.yml
	 * 
	 * @return Se tem armazenamento na storage.yml
	 */
	public boolean hasStorage() {
		return !storage.getKeys().isEmpty();
	}

	public Config getStorage() {
		return storage;
	}

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}

	public Plugin getPluginInstance() {
		return this;
	}

	/**
	 * Ao carregar o plugin no servidor ele inicia as as variaveis
	 */
	public void onLoad() {
		config = new Config(this);
		messages = new Config(this, "messages.yml");
		storage = new Config(this, "storage.yml");
		databaseFile = new File(getDataFolder(), "database.db");
	}

	public void registerPackage(String packname) {
		StorageAPI.registerPackage(getClass(), packname);
	}

	public List<Class<?>> getClasses(String pack) {
		return Mine.getClasses(this, pack);
	}

	public double getPrice() {

		double valor = 0;
		List<Class<?>> classes = Mine.getClasses(this, getClass());
		for (Class<?> claz : classes) {
			double numero = Extra.getValueOf(claz, new ArrayList<>(), false);
			valor += numero;
//			System.out.println("PRECO DA CLASSE "+ claz+ " eh "+ numero);
		}
//		System.out.println("NORMAL PRICE "+valor);
//		if (hasMessages()) {
//			valor += 5;
////			System.out.println("HAVE Messages");
//		}
//		if (isEditable()) {
//			valor += 5;
////			System.out.println("IS Editable");
//		}
//		if (hasStorage()) {
//			valor += 10;
////			System.out.println("HAVE Storage");
//		}
//		System.out.println("HAVE ADICIONAL");
//		valor += 5;
		return valor;

	}

	/**
	 * Gera backup dos arquivos config.yml, storage.yml e por ultimo database.db
	 */
	public void backup() {

		try {

			File pasta = new File(getDataFolder(),
					"/backup/" + DATE_TIME_FORMATER.format(System.currentTimeMillis()) + "/");

			pasta.mkdirs();
			if (getStorage().existConfig() && !getStorage().getKeys().isEmpty()) {

				Files.copy(getStorage().getFile().toPath(), Paths.get(pasta.getPath(), storage.getName()));
			}
			if (getConfigs().existConfig() && !getStorage().getKeys().isEmpty()) {

				Files.copy(getConfigs().getFile().toPath(), Paths.get(pasta.getPath(), config.getName()));
			}
			if (databaseFile.exists()) {
				Files.copy(databaseFile.toPath(), Paths.get(pasta.getPath(), databaseFile.getName()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Executa o metodo startAutoSave com parametro 60 segundos
	 */
	public void startAutoSave() {
		startAutoSave(60);
	}

	/**
	 * Inicia um timer que salva a cada X segundos os dados
	 * 
	 * @param seconds
	 */
	public void startAutoSave(int seconds) {
		asyncTimer(new Runnable() {

			@Override
			public void run() {
				save();
			}
		}, seconds * 20, seconds * 20);
	}

	public void startAutoBackup() {
		startAutoBackup(5 * 60);
	}

	public void startAutoBackup(int seconds) {
		asyncTimer(new Runnable() {

			@Override
			public void run() {
				backup();
			}
		}, seconds * 20, seconds * 20);
	}

	public void save() {

	}

	public void reload() {
	}

	public void configDefault() {

	}

	public boolean getBoolean(String path) {
		return config.getBoolean(path);
	}

	public int getInt(String path) {
		return config.getInt(path);
	}

	public double getDouble(String path) {
		return config.getDouble(path);
	}

	public String message(String path) {
		return messages.message(path);
	}

	public List<String> getMessages(String path) {
		return messages.getMessages(path);
	}

	public Config getMessages() {
		return messages;
	}

	public Config getConfigs() {
		return config;
	}

	public File getDatabaseFile() {
		return databaseFile;
	}

	public void setDatabaseFile(File databaseFile) {
		this.databaseFile = databaseFile;
	}

	public boolean isLogEnabled() {
		return logEnabled;
	}

	public void setLogEnabled(boolean logEnabled) {
		this.logEnabled = logEnabled;
	}

	public boolean isErrorLogEnabled() {
		return errorLogEnabled;
	}

	public void setErrorLogEnabled(boolean errorLogEnabled) {
		this.errorLogEnabled = errorLogEnabled;
	}
}
