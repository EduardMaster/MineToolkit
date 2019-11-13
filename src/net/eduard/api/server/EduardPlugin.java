package net.eduard.api.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.manager.DBManager;
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
	private static final SimpleDateFormat DATE_TIME_FORMATER = new SimpleDateFormat("dd-MM-YYYY HH-mm-ss");
	private boolean logEnabled = true;
	private boolean errorLogEnabled = true;

	protected DBManager db;
	protected Config config;
	protected Config messages;
	protected Config storage;
	private File databaseFile;
	private boolean free;

	public DBManager getDB() {
		return db;
	}

	public String getString(String path) {
		return config.message(path);
	}

	/**
	 * Config padrão do spigot não funciona com {@link Config}
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

	/**
	 *
	 * @return A {@link Config} Storage
	 */
	public Config getStorage() {
		return storage;
	}

	/**
	 * 
	 * @return Se o Plugin é gratuito
	 */
	public boolean isFree() {
		return free;
	}

	/**
	 * Define se o Plugin é gratuito
	 * 
	 * @param free
	 */
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
		config = new Config(this,"config.yml");
		db = new DBManager();
		config.add("auto-save", false);
		config.add("auto-save-seconds", 60);
		config.add("auto-save-lasttime", Extra.getNow());
		config.add("backup", false);
		config.add("backup-lasttime", Extra.getNow());
		config.add("backup-time", 1);
		config.add("backup-timeunit-type", "MINUTES");
		config.add("database", db);
		config.saveConfig();
		messages = new Config(this, "messages.yml");
		storage = new Config(this, "storage.yml");
		databaseFile = new File(getDataFolder(), "database.db");
		reloadDBManager();
	}
	public void reloadDBManager() {
		db = (DBManager) config.get("database");
	}

	public long getAutoSaveSeconds() {
		return config.getLong("auto-save-seconds");
	}

	public boolean isAutoSaving() {
		return config.getBoolean("auto-save");
	}

	public long getBackupTime() {
		return config.getLong("backup-time");
	}

	public long getBackupLastTime() {
		return config.getLong("backup-lasttime");
	}

	public long getAutoSaveLastTime() {
		return config.getLong("auto-save-lasttime");
	}

	public TimeUnit getBackupTimeUnitType() {
		return TimeUnit.valueOf(config.getString("backup-timeunit-type").toUpperCase());
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

	public void autosave() {
		config.set("auto-save-lasttime", Extra.getNow());
		save();
	}

	/**
	 * Deleta os ultimos backups
	 */
	public void deleteLastBackups() {
		File pasta = new File(getDataFolder(), "/backup/");

		pasta.mkdirs();
		List<File> lista = Arrays.asList(pasta.listFiles());
		lista = lista.stream().sorted(Comparator.comparing(File::lastModified)).collect(Collectors.toList());
		for (int i = lista.size() - 10; i >= 0; i--) {
			File arquivo = lista.get(i);
			Extra.deleteFolder(arquivo);
			if (arquivo.exists())
				arquivo.delete();

		}
	}

	/**
	 * 
	 * Gera backup dos arquivos config.yml, storage.yml e por ultimo database.db
	 */
	public void backup() {
		config.set("backup-lasttime", Extra.getNow());
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

	public boolean isBackup() {

		return config.getBoolean("backup");
	}

}
