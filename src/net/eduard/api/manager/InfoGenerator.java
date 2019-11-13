package net.eduard.api.manager;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Effect;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World.Environment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import net.eduard.api.EduardAPI;
import net.eduard.api.lib.config.Config;
import net.eduard.api.lib.config.ConfigSection;

/**
 * Sistema que gera informações do servidor
 * 
 * @author Eduard
 * @since 1.1
 * @version 3.0
 */
public class InfoGenerator {
	private EduardAPI plugin;

	public InfoGenerator(EduardAPI plugin) {
		setPlugin(plugin);
		if (!new File(plugin.getDataFolder(), "DataBase").exists()) {
			saveEnum(DamageCause.class);
			saveEnum(Material.class);
			saveEnum(Effect.class);
			saveEnum(EntityType.class);
			saveEnum(TargetReason.class);
			saveEnum(Sound.class);
			saveEnum(EntityEffect.class);
			saveEnum(Environment.class);
			saveEnum(PotionType.class);
			saveClassLikeEnum(PotionEffectType.class);
			plugin.log("DataBase gerada!");
		} else {
			plugin.log("DataBase ja foi gerada!");
		}

//		if (getConfigs().getBoolean("save-worlds")) {
//			for (World world : Bukkit.getWorlds()) {
//				saveObject("Worlds/" + world.getName(), world);
//			}
//		}
//		if (getConfigs().getBoolean("save-players")) {
//			for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
//				String name = player.getName();
//				saveObject("Players/" + name + " " + player.getUniqueId(), player);
//			}
//		}
//		saveObject("Server", Bukkit.getServer());
	}

	private void saveEnum(Class<?> value) {
		saveEnum(value, false);
	}

	private void saveClassLikeEnum(Class<?> value) {
		try {
			Config config = new Config("DataBase/" , value.getSimpleName() + ".yml");
			for (Field field : value.getFields()) {
				if (field.getType().equals(value)) {
					Object obj = field.get(value);
					ConfigSection section = config.getSection(field.getName());
					for (Method method : obj.getClass().getDeclaredMethods()) {
						String name = method.getName();
						if ((method.getParameterCount() == 0)
								&& name.startsWith("get") | name.startsWith("is") | name.startsWith("can")) {
							method.setAccessible(true);
							Object test = method.invoke(obj);
							if (test instanceof Class)
								continue;
							section.add(method.getName(), test);
						}
					}
				}
			}
			config.saveConfig();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	static void saveObject(String local, Object value) {
		try {
			Config config = new Config("DataBase/" , local + ".yml");
			ConfigSection section = config.getConfig();
			for (Method method : value.getClass().getDeclaredMethods()) {
				String name = method.getName();
				if ((method.getParameterCount() == 0)
						&& name.startsWith("get") | name.startsWith("is") | name.startsWith("can")) {
					method.setAccessible(true);
					if (name.equals("getLoadedChunks")) {
						continue;
					}
					if (name.equals("getOfflinePlayers")) {
						continue;
					}
					if (name.equals("getEntities") || name.equals("getLivingEntities")) {
						continue;
					}
					Object test = method.invoke(value);
					if (test == null)
						continue;
					if (test instanceof Class)
						continue;
//					System.out.println("Chave "+method.getName()+ " Objeto "+test);
					section.set(method.getName(), test);
				}
			}
			config.saveConfig();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void saveEnum(Class<?> value, boolean perConfig) {
		try {
			if (perConfig) {

				for (Object part : value.getEnumConstants()) {

					try {
						Enum<?> obj = (Enum<?>) part;
						Config config = new Config("DataBase/" + value.getSimpleName() + "/" , obj.name() + ".yml");
						ConfigSection section = config.getConfig();
						section.set("number", obj.ordinal());
						for (Method method : obj.getClass().getDeclaredMethods()) {
							String name = method.getName();
							if ((method.getParameterCount() == 0)
									&& name.startsWith("get") | name.startsWith("is") | name.startsWith("can")) {
								method.setAccessible(true);
								Object test = method.invoke(obj);
								if (test instanceof Class)
									continue;
								section.add(method.getName(), test);
							}
						}
						config.saveConfig();
					} catch (Exception ex) {
						ex.printStackTrace();
						continue;
					}
				}

			} else {
				Config config = new Config("DataBase/" , value.getSimpleName() + ".yml");
				boolean used = false;
				for (Object part : value.getEnumConstants()) {
					try {
						Enum<?> obj = (Enum<?>) part;
						ConfigSection section = config.add(obj.name(), obj.ordinal());

						for (Method method : obj.getClass().getDeclaredMethods()) {
							String name = method.getName();
							if ((method.getParameterCount() == 0)
									&& name.startsWith("get") | name.startsWith("is") | name.startsWith("can")) {
								try {
									method.setAccessible(true);
									Object test = method.invoke(obj);
									if (test == null)
										continue;
									if (test instanceof Class)
										continue;
									section.add(method.getName(), test);
									used = true;
								} catch (Exception ex) {
									getPlugin().log("O metodo §c" + name + "§f causou erro!");
									ex.printStackTrace();
									continue;
								}

							}
						}

					} catch (Exception ex) {
						ex.printStackTrace();
						continue;
					}
				}
				if (!used)
					config.setIndent(0);
				config.saveConfig();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public EduardAPI getPlugin() {
		return plugin;
	}

	public void setPlugin(EduardAPI plugin) {
		this.plugin = plugin;
	}

}
