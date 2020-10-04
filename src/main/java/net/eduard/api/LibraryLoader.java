package net.eduard.api;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;

public class LibraryLoader {

    private final File libFile;
    public LibraryLoader(File file){
        this.libFile = file;
    }
    public void loadLibraries(){
        File pastaLibs = libFile;
        pastaLibs.mkdirs();


        log("Starting loading libraries");
        for (File file : Objects.requireNonNull(pastaLibs.listFiles())){
            log("Verifying if is a Jar: "+file.getName());
            if (file.getName().endsWith(".jar")){
                try {
                    log("Loading jar..");
                    addClassPath(getJarUrl(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void log(String msg)
    {

        String name = "EduardAPI JarLoader";
        System.out.println("["+ name +"] "+msg);
    }






    public static URL getJarUrl(final File file) throws IOException {
        return new URL("jar:" + file.toURI().toURL().toExternalForm() + "!/");
    }
    private void addClassPath(final URL url) throws IOException {
        final URLClassLoader sysloader = (URLClassLoader) ClassLoader
                .getSystemClassLoader();
        final Class<URLClassLoader> sysclass = URLClassLoader.class;
        try {
            final Method method = sysclass.getDeclaredMethod("addURL",
                    URL.class);
            method.setAccessible(true);
            method.invoke(sysloader, url);
        } catch (final Throwable t) {
            t.printStackTrace();
            throw new IOException("Error adding " + url
                    + " to system classloader");
        }
    }
}
