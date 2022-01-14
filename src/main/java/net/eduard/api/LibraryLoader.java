package net.eduard.api;

import kotlin.KotlinVersion;
import net.minecraft.server.v1_8_R3.EnchantmentLootBonus;
import org.bukkit.Bukkit;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

/**
 * Carregador de Jars no servidor
 */
public class LibraryLoader {

    private final File libFile;

    /**
     * @return se possui o Koltin carregado
     */
    public boolean needLoadKotlin() {

        try {
            KotlinVersion version = KotlinVersion.CURRENT;
            return false;
        } catch (Error err) {
            return true;
        }

    }

    public LibraryLoader(File file) {
        this.libFile = file;

    }

    /**
     * Tenta Carregar os Jars da pasta
     */
    public void loadLibraries() {
        File pastaLibs = libFile;
        pastaLibs.mkdirs();
        log("Iniciando carregamento de libraries");
        for (File file : Objects.requireNonNull(pastaLibs.listFiles())) {
            if (file.getName().endsWith(".jar")) {
                if (!needLoadKotlin() && file.getName().toLowerCase().contains("kotlin"))
                    continue;
                try {
                    log("Carregando jar: " + file.getName());
                    addClassPath(file);
                } catch (Exception e) {
                    log("Falha no carregamento do: " + file.getName());
                    e.printStackTrace();
                }
            }
        }
    }

    public void log(String msg) {
        String name = "EduardAPI JarLoader";
        System.out.println("[" + name + "] " + msg);
    }

    /**
     * Função que injeta os Jars no ClassPath desta aplicativo rodando em Java
     * @param file File
     * @throws Exception Falhou em carregar os Jars por varios motivos (Não possuir suporte)
     */
    private void addClassPath(final File file) throws Exception {
        URL url = new URL("jar:" + file.toURI().toURL().toExternalForm() + "!/");
        final Object systemClassLoader = ClassLoader
                .getSystemClassLoader();
        final Method method = URLClassLoader.class
                .getDeclaredMethod("addURL",
                        URL.class);
        method.setAccessible(true);
        if (URLClassLoader.class.isAssignableFrom(systemClassLoader.getClass())) {
            method.invoke(systemClassLoader, url);
        } else {
            log("Java incompativel para carregamento de jar automatico");
        }
    }
}
