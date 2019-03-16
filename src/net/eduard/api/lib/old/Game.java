package net.eduard.api.lib.old;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.Vector;

import net.eduard.api.lib.modules.GameAPI;

/**
 * API de mexer com o jogador
 * @deprecated Versão atual {@link GameAPI}
 * @version 1.0
 * @since 0.7
 * @author Eduard
 *
 */
public class Game
{
  private Player player;
  
  public Game(Player player)
  {
    setPlayer(player);
  }
  
  public Player getPlayer()
  {
    return this.player;
  }
  


  private void setPlayer(Player player) { this.player = player; }
  
  public void resetOptions() {
    getPlayer().resetPlayerWeather();
    getPlayer().resetPlayerTime();
    getPlayer().resetMaxHealth();
  }
  
  public void stop() { getPlayer().setVelocity(new Vector()); }
  
  public void normalyze()
  {
    getPlayer().setWalkSpeed(0.2F);
    getPlayer().setFlySpeed(0.05F);
  }
  
  public void refreshAll() {
    normalyze();
    resetOptions();
    resfreshPotion();
    refreshFood();
    refreshLife();
  }
  

  public void makeInvul(int seconds) { getPlayer().setNoDamageTicks(20 * seconds); }
  
  public void resfreshPotion() {
    for (PotionEffect pt : getPlayer().getActivePotionEffects()) {
      getPlayer().removePotionEffect(pt.getType());
    }
  }
  
  public void refreshLife() { getPlayer().setHealth(getPlayer().getMaxHealth()); }
  
  public void refreshFood() {
    getPlayer().setFoodLevel(20);
    getPlayer().setExhaustion(0.0F);
    getPlayer().setSaturation(20.0F);
  }
  
  public void removeArmours() { getPlayer().getInventory().setArmorContents(null); }
  

  public void removeItems() { getPlayer().getInventory().clear(); }
  
  public void removeItemHand() {
    if (getPlayer().getInventory() != null)
      getPlayer().getItemInHand().setType(Material.AIR);
  }
  
  public void clearInventory() { removeArmours();
    removeItemHand();
    removeItems();
  }
}
