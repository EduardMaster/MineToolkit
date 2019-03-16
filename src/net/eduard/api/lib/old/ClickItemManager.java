package net.eduard.api.lib.old;

import org.bukkit.event.player.PlayerInteractEvent;

import net.eduard.api.lib.click.PlayerClickEffect;
/**
 * Representa o efeito que vai acontecer quando o player clicar com um Item
 * @version 1.0
 * @since 0.7
 * @author Eduard
 * @deprecated Versão atual {@link PlayerClickEffect}
 *
 */
public abstract interface ClickItemManager
{
  public abstract void clickEvent(PlayerInteractEvent event);
}
