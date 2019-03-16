package net.eduard.api.lib.old;

import org.bukkit.plugin.java.JavaPlugin;
/**
 * Interface com métodos auxiliares de marcação de ativação e método getInstance() que puxa quem carregou esta classe
 * @since EduardAPI 0.7
 * @version 1.0
 * @author Eduard
 *
 */
public abstract interface ActiveManager
{
  public abstract boolean isActive();
  
  public abstract void setActive(boolean paramBoolean);
  public default JavaPlugin getInstance() {
	  return JavaPlugin.getProvidingPlugin(getClass());
  }
}
