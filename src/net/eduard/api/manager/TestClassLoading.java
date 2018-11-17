package net.eduard.api.manager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.Bukkit;
/**
 * 
 * Donos Will e Hero
 *
 */
public class TestClassLoading {
	
	    public TestClassLoading() {
	        ClassLoader pClassLoader = CustomClassLoader.class.getClassLoader();

	        try {
	        	Object ligado = new CustomClassLoader(pClassLoader)
	                    .loadClass("CustomClass:http://heroslender.cf/classes/SuperTop.class|net.hipercraft.SuperTop")
	                    .newInstance();

	            Bukkit.getLogger().info("Valor: " + ligado);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    public class CustomClassLoader extends ClassLoader {
	        public CustomClassLoader(ClassLoader parent) {
	            super(parent);
	        }

	        public Class<?> loadClass(String cmdName) throws ClassNotFoundException {
	        /*
	        Exemplo:
	        CustomClass:http://heroslender.cf/classes/SuperTop.class|cf.heroslender.myplugin.SuperTop
	         */
	            if (!cmdName.startsWith("CustomClass:") || !cmdName.contains("|"))
	                return super.loadClass(cmdName);

	            cmdName = cmdName.replace("CustomClass:", "");
	            Bukkit.getLogger().info(cmdName);
	            try {
	                String url = cmdName.split("\\|")[0];
	                Bukkit.getLogger().info(url);
	                URL myUrl = new URL(url);
	                URLConnection connection = myUrl.openConnection();
	                InputStream input = connection.getInputStream();
	                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	                int data = input.read();

	                while (data != -1) {
	                    buffer.write(data);
	                    data = input.read();
	                }

	                input.close();

	                byte[] classData = buffer.toByteArray();

	                return defineClass(cmdName.split("\\|")[1],
	                        classData, 0, classData.length);

	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return null;
	        }
	    }
}
