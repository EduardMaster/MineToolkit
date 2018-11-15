package net.eduard.api.manager;

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
	
}
