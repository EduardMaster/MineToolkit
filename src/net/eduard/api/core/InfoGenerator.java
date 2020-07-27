package net.eduard.api.core;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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
 * Sistema que gera informações do Bukkit
 *
 * @author Eduard
 * @version 4.0
 * @since 1.1
 */
public class InfoGenerator {
    private EduardAPI plugin;

    public InfoGenerator(EduardAPI plugin) {
        setPlugin(plugin);
        File pasta = new File(plugin.getPluginFolder(), "database");
        pasta.mkdirs();
        if (pasta.listFiles().length == 0) {
            saveEnum(DamageCause.class);
            saveEnum(Material.class);
            saveEnum(Effect.class);
            saveEnum(EntityType.class,"getKey");
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

    }


    private void saveClassLikeEnum(Class<?> value) {
        try {
            Config config = new Config(plugin,"DataBase/"+ value.getSimpleName() + ".yml");
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


    private void saveEnum(Class<?> value, String... ignoredMethods) {
        try {

            Config config = new Config(plugin,"DataBase/"+ value.getSimpleName() + ".yml");
            boolean used = false;
            for (Object part : value.getEnumConstants()) {
                try {
                    Enum<?> obj = (Enum<?>) part;
                    ConfigSection section = config.add(obj.name(), obj.ordinal());
                        if (obj.name().startsWith("LEGACY")){
                           // getPlugin().log("Ignorando Enum Legacy que não suporta mais ID: "+obj.name());
                            continue;
                        }
                    inicial: for (Method method : obj.getClass().getDeclaredMethods()) {
                        String name = method.getName();
                        if (Modifier.isStatic(method.getModifiers())) continue;
                        for (String metName : ignoredMethods) {
                            if (metName.equals(name)) {
                                continue inicial;
                            }
                        }

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
                                //getPlugin().log("O metodo §c" + name + "§f causou erro! §f" + ex.getMessage());
                                //ex.printStackTrace();

                            }

                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (!used)
                config.setIndent(0);
            config.saveConfig();
				/*
			}

			*/


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
